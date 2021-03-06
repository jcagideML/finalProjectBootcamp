package com.bootcamp.finalProject.services;

import com.bootcamp.finalProject.dtos.*;
import com.bootcamp.finalProject.exceptions.*;
import com.bootcamp.finalProject.mnemonics.DeliveryStatus;
import com.bootcamp.finalProject.model.*;
import com.bootcamp.finalProject.repositories.ISubsidiaryRepository;
import com.bootcamp.finalProject.repositories.ISubsidiaryStockRepository;
import com.bootcamp.finalProject.repositories.OrderRepository;
import com.bootcamp.finalProject.repositories.PartRepository;
import com.bootcamp.finalProject.utils.OrderNumberCMUtil;
import com.bootcamp.finalProject.utils.OrderResponseMapper;
import com.bootcamp.finalProject.utils.SubsidiaryResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.bootcamp.finalProject.utils.MapperUtils.completeNumberByLength;
import static com.bootcamp.finalProject.utils.MapperUtils.getDifferencesInDays;
import static com.bootcamp.finalProject.utils.ValidationPartUtils.DSOrderTypeValidation;
import static com.bootcamp.finalProject.utils.ValidationPartUtils.deliveryStatusValidation;

@Service
public class WarehouseService implements IWarehouseService {

    SubsidiaryResponseMapper subsidiaryMapper = new SubsidiaryResponseMapper();
    OrderResponseMapper orderMapper = new OrderResponseMapper();

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PartRepository partRepository;

    @Autowired
    private ISubsidiaryRepository subsidiaryRepository;

    @Autowired
    private ISubsidiaryStockRepository subsidiaryStockRepository;

    @Autowired
    private IUserService userService;

    public WarehouseService() {
        initTask();
    }

    private void initTask() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                checkDelayedOrders();
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 1000L * 60L; //The delay time. The task is executed one minute after the app start.
        long period = 1000L * 60L * 60L * 12L; //The task is executed two times for day. (millis*seconds*minutes*hours).
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    @Override
    public SubsidiaryResponseDTO findSubsidiaryOrders(OrderRequestDTO orderRequest) throws OrderTypeException, DeliveryStatusException, SubsidiaryNotFoundException {
        List<Order> orders;
        Subsidiary subsidiary;
        if (deliveryStatusValidation(orderRequest.getDeliveryStatus())) {
            Sort sort = DSOrderTypeValidation(orderRequest.getOrder());
            Long idSubsidiary = orderRequest.getDealerNumber();
            subsidiary = subsidiaryRepository.findById(idSubsidiary).orElseThrow(SubsidiaryNotFoundException::new);
            if (orderRequest.getDeliveryStatus() == null) {
                orders = orderRepository.findByIdSubsidiary(idSubsidiary, sort);
            } else {
                orders = orderRepository.findByIdSubsidiaryAndDeliveryStatus(idSubsidiary, orderRequest.getDeliveryStatus(), sort);
            }
            subsidiary.setOrders(orders);
        } else {
            throw new DeliveryStatusException();
        }
        return subsidiaryMapper.toOrderDTO(subsidiary);
    }

    @Override
    public OrderResponseDTO findByOrderNumberCM(String orderNumberCM) throws OrderIdNotFoundException, SubsidiaryNotFoundException {
        Long id = Long.valueOf(OrderNumberCMUtil.getNumberOR(orderNumberCM));

        Long idSubsidiary = Long.valueOf(OrderNumberCMUtil.getNumberCE(orderNumberCM));

        Subsidiary s = subsidiaryRepository.findById(idSubsidiary).orElseThrow(SubsidiaryNotFoundException::new);
        Order o = orderRepository.findByIdOrderAndSubsidiary(id, s).orElseThrow(OrderIdNotFoundException::new);
        return new OrderResponseMapper().toOrderNumberCMDTO(o, s.getIdSubsidiary());
    }

    @Override
    public SubsidiaryStockResponseDTO findSubsidiaryStock(SubsidiaryStockRequestDTO subsidiaryStockRequestDTO) throws SubsidiaryNotFoundException {

        Subsidiary subsidiary = subsidiaryRepository.findById(subsidiaryStockRequestDTO.getDealerNumber()).orElseThrow(SubsidiaryNotFoundException::new);

        return new SubsidiaryResponseMapper().toStockDTO(subsidiary);
    }

    public OrderDTO newOrder(OrderDTO order, UserDetails user) throws InvalidAccountTypeExtensionException, NotEnoughStock, PartNotExistException {
        Subsidiary subsidiary = userService.getSubsidiaryByUsername(user);

        Date current = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);

        calendar.add(Calendar.DAY_OF_YEAR, subsidiary.getDaysToShipping());

        Order orderReturn = new Order(null, current, calendar.getTime(), null, DeliveryStatus.PENDING, null, subsidiary);

        List<OrderDetail> orderList = new ArrayList<>();
        List<OrderDetailDTO> orderDetailList = order.getOrderDetails();
        for (OrderDetailDTO orderDetail : orderDetailList) {
            orderList.add(validateOrderToCreate(orderDetail, orderReturn));
            orderDetail.setReason(null);
        }
        orderReturn.setOrderDetails(orderList);
        orderRepository.save(orderReturn);

        for (OrderDetail orderDetail : orderReturn.getOrderDetails()) {
            Part partStock = orderDetail.getPartOrder();
            partStock.setQuantity(partStock.getQuantity() - orderDetail.getQuantity());
            partRepository.save(partStock);
        }

        SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");

        order.setOrderNumberCM(completeNumberByLength(String.valueOf(subsidiary.getIdSubsidiary()), 4) + "-" + completeNumberByLength(String.valueOf(orderReturn.getIdOrder()), 8));
        order.setOrderDate(datePattern.format(orderReturn.getOrderDate()));
        order.setDeliveryDate(datePattern.format(calendar.getTime()));
        order.setDeliveryStatus(DeliveryStatus.PENDING);
        order.setDaysDelayed(getDifferencesInDays(orderReturn.getDeliveryDate(), orderReturn.getDeliveredDate()));
        return order;
    }

    private OrderDetail validateOrderToCreate(OrderDetailDTO orderDetail, Order orderReturn) throws PartNotExistException, NotEnoughStock, InvalidAccountTypeExtensionException {
        OrderDetail orderDetailReturn;
        Part part = partRepository.findByPartCode(Integer.parseInt(orderDetail.getPartCode()));
        if (part == null) {
            throw new PartNotExistException(Integer.parseInt(orderDetail.getPartCode()));
        } else {
            if (part.getQuantity() < orderDetail.getQuantity()) {
                throw new NotEnoughStock(orderDetail.getPartCode());
            } else {
                orderDetail.setDescription(part.getDescription());

                if (orderDetail.getAccountType() == null || orderDetail.getAccountType().length() != 1) {
                    throw new InvalidAccountTypeExtensionException();
                }
            }
            orderDetailReturn = new OrderDetail(null, orderDetail.getAccountType(), orderDetail.getQuantity(), null, part, orderReturn);
        }
        return orderDetailReturn;
    }

    public void changeDeliveryStatus(String orderNumberCM, String newStatus) throws InternalExceptionHandler {
        Long orderId = Long.valueOf(OrderNumberCMUtil.getNumberOR(orderNumberCM));
        Long idSubsidiary = Long.valueOf(OrderNumberCMUtil.getNumberCE(orderNumberCM));

        Subsidiary subsidiary = subsidiaryRepository.findById(idSubsidiary).orElseThrow(SubsidiaryNotFoundException::new);
        Order order = orderRepository.findByIdOrderAndSubsidiary(orderId, subsidiary).orElseThrow(OrderIdNotFoundException::new);

        if ((order.getDeliveryStatus().equals(DeliveryStatus.PENDING)
                || order.getDeliveryStatus().equals(DeliveryStatus.DELAYED))) {

            switch (newStatus) {
                case DeliveryStatus.DELAYED:
                    order.setDeliveryStatus(DeliveryStatus.DELAYED);
                    break;
                case DeliveryStatus.CANCELED:
                    cancelDeliveryStatus(order);
                    break;
                case DeliveryStatus.FINISHED:
                    finishDeliveryStatus(order, subsidiary);
                    break;
            }
            orderRepository.save(order);
        } else {
            //excepcion de que la orden habia sido cancelada o finalizada con anterioridad
            throw new OrderDeliveryStatusIsconcludedException(order.getDeliveryStatus());
        }
    }

    public void cancelDeliveryStatus(Order order) {
        List<OrderDetail> orderDetail = order.getOrderDetails();

        orderDetail.forEach(partToUpdate -> {
            Part part = partRepository.findById(partToUpdate.getPartOrder().getIdPart()).orElseThrow();
            part.setQuantity(part.getQuantity() + partToUpdate.getQuantity());
            partRepository.save(part);
        });
        order.setDeliveryStatus(DeliveryStatus.CANCELED);
        orderRepository.save(order);
    }

    public void finishDeliveryStatus(Order order, Subsidiary subsidiary) {
        List<OrderDetail> orderDetails = order.getOrderDetails();
        SubsidiaryStock temp;
        for (OrderDetail o : orderDetails) {
            temp = subsidiaryStockRepository.findByIdPart(o.getPartOrder().getIdPart(), subsidiary.getIdSubsidiary());
            if (temp == null) {
                SubsidiaryStock subsidiaryStock = new SubsidiaryStock();
                subsidiaryStock.setQuantity(0);
                subsidiaryStock.setPart(o.getPartOrder());
                subsidiaryStock.setSubsidiary(subsidiary);
                subsidiaryStockRepository.save(subsidiaryStock);
                temp = subsidiaryStock;
            }
            temp.setQuantity(temp.getQuantity() + o.getQuantity());
            subsidiaryStockRepository.save(temp);
        }
        Date now = new Date();
        order.setDeliveryStatus(DeliveryStatus.FINISHED);
        order.setDeliveredDate(now);
    }


    public List<OrderDTO> checkDelayedOrders() {
        List<Order> orders = orderRepository.findAll();
        List<Order> changed = new ArrayList<>();
        for (Order o : orders) {
            if (o.getDeliveryStatus().equals(DeliveryStatus.PENDING)) {
                if (o.getDeliveryDate().before(new Date())) {
                    o.setDeliveryStatus(DeliveryStatus.DELAYED);
                    changed.add(o);
                    orderRepository.save(o);
                }
            }
        }
        return orderMapper.orderDTOList(changed);
    }
}