package com.bootcamp.finalProject.services;

import com.bootcamp.finalProject.dtos.*;
import com.bootcamp.finalProject.exceptions.*;
import com.bootcamp.finalProject.mnemonics.DeliveryStatus;
import com.bootcamp.finalProject.model.*;
import com.bootcamp.finalProject.repositories.ISubsidiaryRepository;
import com.bootcamp.finalProject.repositories.ISubsidiaryStockRepository;
import com.bootcamp.finalProject.repositories.OrderRepository;
import com.bootcamp.finalProject.repositories.PartRepository;
import com.bootcamp.finalProject.utils.SubsidiaryResponseMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;


public class WarehouseServiceTest {

    @InjectMocks
    WarehouseService warehouseService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    PartRepository partRepository;

    @Mock
    ISubsidiaryRepository subsidiaryRepository;

    @Mock
    ISubsidiaryStockRepository subsidiaryStockRepository;

    @Mock
    SubsidiaryResponseMapper mapper;

    @Mock
    UserService userService;

    UserDetails user;

    @BeforeEach
    void initSetUp() {
        openMocks(this);

        user = org.springframework.security.core.userdetails.User
                .withUsername("userName")
                .password("pass")
                .authorities(new ArrayList<>())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    @Test
    public void findSubsidiaryOrdersThrowsSubsidiaryNotFoundException() {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setOrder(1);
        requestDTO.setDeliveryStatus("P");
        requestDTO.setDealerNumber(0L);

        //Act and Assert
        when(subsidiaryRepository.findById(requestDTO.getDealerNumber())).thenReturn(Optional.empty());

        Assertions.assertThrows(SubsidiaryNotFoundException.class, () -> warehouseService.findSubsidiaryOrders(requestDTO));
    }

    @Test
    public void findSubsidiaryOrdersThrowsSubsidiaryNotFoundExceptionDealerNumberNull() {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setOrder(1);
        requestDTO.setDeliveryStatus("P");
        requestDTO.setDealerNumber(null);

        //Act and Assert
        Assertions.assertThrows(SubsidiaryNotFoundException.class, () -> warehouseService.findSubsidiaryOrders(requestDTO));
    }

    @Test
    public void findSubsidiaryOrdersThrowsDeliveryStatusExceptionDiffValue() {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setDeliveryStatus("A");

        //Act and Assert
        Assertions.assertThrows(DeliveryStatusException.class, () -> warehouseService.findSubsidiaryOrders(requestDTO));
    }

    @Test
    public void findSubsidiaryOrdersDeliveryStatusNull() throws SubsidiaryNotFoundException, OrderTypeException, DeliveryStatusException {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setOrder(1);
        requestDTO.setDealerNumber(1L);

        Subsidiary subsidiary = new Subsidiary();
        List<Order> orders = new ArrayList<>();

        SubsidiaryResponseDTO expected = new SubsidiaryResponseDTO();
        List<OrderDTO> ordersDTO = new ArrayList<>();
        expected.setDealerNumber("");
        expected.setOrders(ordersDTO);

        //Act
        when(subsidiaryRepository.findById(requestDTO.getDealerNumber())).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdSubsidiary(requestDTO.getDealerNumber(), Sort.by(Sort.Direction.ASC, "orderDate"))).thenReturn(orders);
        when(mapper.toOrderDTO(subsidiary)).thenReturn(expected);

        SubsidiaryResponseDTO actual = warehouseService.findSubsidiaryOrders(requestDTO);

        //Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findSubsidiaryOrdersDeliveryStatusP() throws SubsidiaryNotFoundException, OrderTypeException, DeliveryStatusException {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setDealerNumber(1L);
        requestDTO.setDeliveryStatus("P");
        requestDTO.setOrder(1);

        Subsidiary subsidiary = new Subsidiary();
        List<Order> orders = new ArrayList<>();

        SubsidiaryResponseDTO expected = new SubsidiaryResponseDTO();
        List<OrderDTO> ordersDTO = new ArrayList<>();
        expected.setDealerNumber("");
        expected.setOrders(ordersDTO);

        //Act
        when(subsidiaryRepository.findById(requestDTO.getDealerNumber())).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdSubsidiaryAndDeliveryStatus(requestDTO.getDealerNumber(), requestDTO.getDeliveryStatus(), Sort.by(Sort.Direction.ASC, "orderDate"))).thenReturn(orders);
        when(mapper.toOrderDTO(subsidiary)).thenReturn(expected);

        SubsidiaryResponseDTO actual = warehouseService.findSubsidiaryOrders(requestDTO);

        //Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findSubsidiaryOrdersDeliveryStatusD() throws SubsidiaryNotFoundException, OrderTypeException, DeliveryStatusException {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setDealerNumber(1L);
        requestDTO.setDeliveryStatus("D");
        requestDTO.setOrder(1);

        Subsidiary subsidiary = new Subsidiary();
        List<Order> orders = new ArrayList<>();

        SubsidiaryResponseDTO expected = new SubsidiaryResponseDTO();
        List<OrderDTO> ordersDTO = new ArrayList<>();
        expected.setDealerNumber("");
        expected.setOrders(ordersDTO);

        //Act
        when(subsidiaryRepository.findById(requestDTO.getDealerNumber())).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdSubsidiaryAndDeliveryStatus(requestDTO.getDealerNumber(), requestDTO.getDeliveryStatus(), Sort.by(Sort.Direction.ASC, "orderDate"))).thenReturn(orders);
        when(mapper.toOrderDTO(subsidiary)).thenReturn(expected);

        SubsidiaryResponseDTO actual = warehouseService.findSubsidiaryOrders(requestDTO);

        //Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findSubsidiaryOrdersDeliveryStatusF() throws SubsidiaryNotFoundException, OrderTypeException, DeliveryStatusException {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setDealerNumber(1L);
        requestDTO.setDeliveryStatus("F");
        requestDTO.setOrder(1);

        Subsidiary subsidiary = new Subsidiary();
        List<Order> orders = new ArrayList<>();

        SubsidiaryResponseDTO expected = new SubsidiaryResponseDTO();
        List<OrderDTO> ordersDTO = new ArrayList<>();
        expected.setDealerNumber("");
        expected.setOrders(ordersDTO);

        //Act
        when(subsidiaryRepository.findById(requestDTO.getDealerNumber())).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdSubsidiaryAndDeliveryStatus(requestDTO.getDealerNumber(), requestDTO.getDeliveryStatus(), Sort.by(Sort.Direction.ASC, "orderDate"))).thenReturn(orders);
        when(mapper.toOrderDTO(subsidiary)).thenReturn(expected);

        SubsidiaryResponseDTO actual = warehouseService.findSubsidiaryOrders(requestDTO);

        //Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findSubsidiaryOrdersDeliveryStatusC() throws SubsidiaryNotFoundException, OrderTypeException, DeliveryStatusException {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setDealerNumber(1L);
        requestDTO.setDeliveryStatus("C");
        requestDTO.setOrder(1);

        Subsidiary subsidiary = new Subsidiary();
        List<Order> orders = new ArrayList<>();

        SubsidiaryResponseDTO expected = new SubsidiaryResponseDTO();
        List<OrderDTO> ordersDTO = new ArrayList<>();
        expected.setDealerNumber("");
        expected.setOrders(ordersDTO);

        //Act
        when(subsidiaryRepository.findById(requestDTO.getDealerNumber())).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdSubsidiaryAndDeliveryStatus(requestDTO.getDealerNumber(), requestDTO.getDeliveryStatus(), Sort.by(Sort.Direction.ASC, "orderDate"))).thenReturn(orders);
        when(mapper.toOrderDTO(subsidiary)).thenReturn(expected);

        SubsidiaryResponseDTO actual = warehouseService.findSubsidiaryOrders(requestDTO);

        //Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findSubsidiaryOrdersThrowsOrderExceptionDiffValue() {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setDeliveryStatus("C");
        requestDTO.setOrder(3);

        //Act and Assert
        Assertions.assertThrows(OrderTypeException.class, () -> warehouseService.findSubsidiaryOrders(requestDTO));
    }

    @Test
    public void findSubsidiaryOrdersByOrder0() throws SubsidiaryNotFoundException, OrderTypeException, DeliveryStatusException {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setDealerNumber(1L);
        requestDTO.setOrder(0);

        Subsidiary subsidiary = new Subsidiary();
        List<Order> orders = new ArrayList<>();

        SubsidiaryResponseDTO expected = new SubsidiaryResponseDTO();
        List<OrderDTO> ordersDTO = new ArrayList<>();
        expected.setDealerNumber("");
        expected.setOrders(ordersDTO);

        //Act
        when(subsidiaryRepository.findById(requestDTO.getDealerNumber())).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdSubsidiary(requestDTO.getDealerNumber(), Sort.by(Sort.Direction.ASC, "idOrder"))).thenReturn(orders);
        when(mapper.toOrderDTO(subsidiary)).thenReturn(expected);

        SubsidiaryResponseDTO actual = warehouseService.findSubsidiaryOrders(requestDTO);

        //Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findSubsidiaryOrdersByOrder1() throws SubsidiaryNotFoundException, OrderTypeException, DeliveryStatusException {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setDealerNumber(1L);
        requestDTO.setOrder(1);

        Subsidiary subsidiary = new Subsidiary();
        List<Order> orders = new ArrayList<>();

        SubsidiaryResponseDTO expected = new SubsidiaryResponseDTO();
        List<OrderDTO> ordersDTO = new ArrayList<>();
        expected.setDealerNumber("");
        expected.setOrders(ordersDTO);

        //Act
        when(subsidiaryRepository.findById(requestDTO.getDealerNumber())).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdSubsidiary(requestDTO.getDealerNumber(), Sort.by(Sort.Direction.ASC, "orderDate"))).thenReturn(orders);
        when(mapper.toOrderDTO(subsidiary)).thenReturn(expected);

        SubsidiaryResponseDTO actual = warehouseService.findSubsidiaryOrders(requestDTO);

        //Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findSubsidiaryOrdersByOrder2() throws SubsidiaryNotFoundException, OrderTypeException, DeliveryStatusException {
        //Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setDealerNumber(1L);
        requestDTO.setOrder(2);

        Subsidiary subsidiary = new Subsidiary();
        List<Order> orders = new ArrayList<>();

        SubsidiaryResponseDTO expected = new SubsidiaryResponseDTO();
        List<OrderDTO> ordersDTO = new ArrayList<>();
        expected.setDealerNumber("");
        expected.setOrders(ordersDTO);

        //Act
        when(subsidiaryRepository.findById(requestDTO.getDealerNumber())).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdSubsidiary(requestDTO.getDealerNumber(), Sort.by(Sort.Direction.DESC, "orderDate"))).thenReturn(orders);
        when(mapper.toOrderDTO(subsidiary)).thenReturn(expected);

        SubsidiaryResponseDTO actual = warehouseService.findSubsidiaryOrders(requestDTO);

        //Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findOrderByNumberThrowsSubsidiaryNotFoundException() {
        //Arrange
        String orderNumber = "0001-00000001";

        //Act and Assert
        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(SubsidiaryNotFoundException.class, () -> warehouseService.findByOrderNumberCM(orderNumber));
    }

    @Test
    public void findOrderByNumberThrowsIdOrderNotFoundException() {
        //Arrange
        String orderNumber = "0001-00000001";
        Subsidiary subsidiary = new Subsidiary();

        //Act and Assert
        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdOrderAndSubsidiary(1L, subsidiary)).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderIdNotFoundException.class, () -> warehouseService.findByOrderNumberCM(orderNumber));
    }


    @Test
    public void findOrderByNumber() throws OrderIdNotFoundException, SubsidiaryNotFoundException {
        //Arrange
        SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");

        String orderNumber = "0001-00000001";
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setIdSubsidiary(1L);

        Order order = new Order();
        order.setIdOrder(1L);
        order.setOrderDate(new Date());
        order.setDeliveryStatus("C");
        order.setOrderDetails(new ArrayList<>());

        OrderResponseDTO expected = new OrderResponseDTO();
        expected.setOrderNumberCM(orderNumber);
        expected.setOrderDate(datePattern.format(order.getOrderDate()));
        expected.setDeliveryStatus(order.getDeliveryStatus());
        expected.setOrderDetails(new ArrayList<>());

        //Act
        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdOrderAndSubsidiary(1L, subsidiary)).thenReturn(Optional.of(order));

        OrderResponseDTO actual = warehouseService.findByOrderNumberCM(orderNumber);

        //Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void newOrder() throws PartNotExistException, NotEnoughStock, InvalidAccountTypeExtensionException {
        //aca hace falta mockear el subsidiary.
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setDaysToShipping(10);

        //Arrange
        Order order = new Order();
        order.setIdOrder(1L);
        order.setSubsidiary(subsidiary);
        order.setOrderDate(new Date());
        order.setDeliveryStatus("P");
        OrderDetail orderDetail = new OrderDetail();
        Part part = new Part();
        part.setPartCode(11111112);
        part.setQuantity(30);
        part.setDescription("Amortiguador delantero derecho - BMW 220i");
        orderDetail.setPartOrder(part);
        orderDetail.setQuantity(20);
        orderDetail.setAccountType("R");
        orderDetail.setIdOrderDetail(18L);
        List<OrderDetail> orderList = new ArrayList<>();
        orderList.add(orderDetail);
        order.setOrderDetails(orderList);

        OrderDTO orderDTOExpected = new OrderDTO();
        SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
        orderDTOExpected.setOrderDate(datePattern.format(new Date()));
        orderDTOExpected.setDeliveryStatus("P");
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        PartDTO partDTO = new PartDTO();
        partDTO.setPartCode(11111112);
        partDTO.setDescription("Amortiguador delantero derecho - BMW 220i");
        partDTO.setQuantity(30);
        orderDetailDTO.setPartCode(String.valueOf(partDTO.getPartCode()));
        orderDetailDTO.setQuantity(20);
        orderDetailDTO.setAccountType("R");
        orderDetailDTO.setDescription(partDTO.getDescription());
        List<OrderDetailDTO> orderListDTO = new ArrayList<>();
        orderListDTO.add(orderDetailDTO);
        orderDTOExpected.setOrderDetails(orderListDTO);

        //Act
        when(partRepository.findByPartCode(11111112)).thenReturn(part);
        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.of(subsidiary));
        when(orderRepository.save(order)).thenReturn(order);
        when(userService.getSubsidiaryByUsername(user)).thenReturn(subsidiary);

        OrderDTO actual = warehouseService.newOrder(orderDTOExpected, user);

        //Assert
        Assertions.assertEquals(orderDTOExpected, actual);
    }

    @Test
    public void newOrderWithoutPart() {
        //aca hace falta mockear el subsidiary.
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setDaysToShipping(10);

        //Arrange
        Order order = new Order();
        order.setIdOrder(1L);
        order.setSubsidiary(subsidiary);
        order.setOrderDate(new Date());
        order.setDeliveryStatus("P");
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setQuantity(20);
        orderDetail.setAccountType("R");
        orderDetail.setIdOrderDetail(18L);
        List<OrderDetail> orderList = new ArrayList<>();
        orderList.add(orderDetail);
        order.setOrderDetails(orderList);

        OrderDTO orderDTO = new OrderDTO();
        SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
        orderDTO.setOrderDate(datePattern.format(new Date()));
        orderDTO.setDeliveryStatus("P");
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        PartDTO partDTO = new PartDTO();
        partDTO.setPartCode(11111112);
        partDTO.setDescription("Amortiguador delantero derecho - BMW 220i");
        partDTO.setQuantity(30);
        orderDetailDTO.setPartCode(String.valueOf(partDTO.getPartCode()));
        orderDetailDTO.setQuantity(20);
        orderDetailDTO.setAccountType("R");
        orderDetailDTO.setDescription(partDTO.getDescription());
        List<OrderDetailDTO> orderListDTO = new ArrayList<>();
        orderListDTO.add(orderDetailDTO);
        orderDTO.setOrderDetails(orderListDTO);

        //Act
        when(partRepository.findByPartCode(11111112)).thenReturn(null);
        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.of(subsidiary));
        when(orderRepository.save(order)).thenReturn(order);
        when(userService.getSubsidiaryByUsername(user)).thenReturn(subsidiary);

        //Assert
        Assertions.assertThrows(PartNotExistException.class, () -> warehouseService.newOrder(orderDTO, user));
    }

    @Test
    public void newOrderNotEnoughStock() {
        //aca hace falta mockear el subsidiary.
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setDaysToShipping(10);

        //Arrange
        Order order = new Order();
        order.setIdOrder(1L);
        order.setSubsidiary(subsidiary);
        order.setOrderDate(new Date());
        order.setDeliveryStatus("P");
        Part part = new Part();
        part.setPartCode(11111112);
        part.setQuantity(0);
        part.setDescription("Amortiguador delantero derecho - BMW 220i");
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPartOrder(part);
        orderDetail.setQuantity(20);
        orderDetail.setAccountType("R");
        orderDetail.setIdOrderDetail(18L);
        List<OrderDetail> orderList = new ArrayList<>();
        orderList.add(orderDetail);
        order.setOrderDetails(orderList);

        OrderDTO orderDTO = new OrderDTO();
        SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
        orderDTO.setOrderDate(datePattern.format(new Date()));
        orderDTO.setDeliveryStatus("P");
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        PartDTO partDTO = new PartDTO();
        partDTO.setPartCode(11111112);
        partDTO.setDescription("Amortiguador delantero derecho - BMW 220i");
        partDTO.setQuantity(30);
        orderDetailDTO.setPartCode(String.valueOf(partDTO.getPartCode()));
        orderDetailDTO.setQuantity(20);
        orderDetailDTO.setAccountType("R");
        orderDetailDTO.setDescription(partDTO.getDescription());
        List<OrderDetailDTO> orderListDTO = new ArrayList<>();
        orderListDTO.add(orderDetailDTO);
        orderDTO.setOrderDetails(orderListDTO);

        //Act
        when(partRepository.findByPartCode(11111112)).thenReturn(part);
        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.of(subsidiary));
        when(orderRepository.save(order)).thenReturn(order);
        when(userService.getSubsidiaryByUsername(user)).thenReturn(subsidiary);

        //Assert
        Assertions.assertThrows(NotEnoughStock.class, () -> warehouseService.newOrder(orderDTO, user));
    }

    @Test
    public void newOrderInvalidAccountType() {
        //aca hace falta mockear el subsidiary.
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setDaysToShipping(10);
        //Arrange
        Order order = new Order();
        order.setIdOrder(1L);
        order.setSubsidiary(subsidiary);
        order.setOrderDate(new Date());
        order.setDeliveryStatus("P");
        Part part = new Part();
        part.setPartCode(11111112);
        part.setQuantity(30);
        part.setDescription("Amortiguador delantero derecho - BMW 220i");
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPartOrder(part);
        orderDetail.setQuantity(20);
        orderDetail.setIdOrderDetail(18L);
        List<OrderDetail> orderList = new ArrayList<>();
        orderList.add(orderDetail);
        order.setOrderDetails(orderList);

        OrderDTO orderDTO = new OrderDTO();
        SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
        orderDTO.setOrderDate(datePattern.format(new Date()));
        orderDTO.setDeliveryStatus("P");
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        PartDTO partDTO = new PartDTO();
        partDTO.setPartCode(11111112);
        partDTO.setDescription("Amortiguador delantero derecho - BMW 220i");
        partDTO.setQuantity(30);
        orderDetailDTO.setPartCode(String.valueOf(partDTO.getPartCode()));
        orderDetailDTO.setQuantity(20);
        orderDetailDTO.setDescription(partDTO.getDescription());
        List<OrderDetailDTO> orderListDTO = new ArrayList<>();
        orderListDTO.add(orderDetailDTO);
        orderDTO.setOrderDetails(orderListDTO);

        //Act
        when(partRepository.findByPartCode(11111112)).thenReturn(part);
        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.of(subsidiary));
        when(orderRepository.save(order)).thenReturn(order);
        when(userService.getSubsidiaryByUsername(user)).thenReturn(subsidiary);

        //Assert
        Assertions.assertThrows(InvalidAccountTypeExtensionException.class, () -> warehouseService.newOrder(orderDTO, user));
    }

    @Test
    public void findSubsidiaryStock() throws SubsidiaryNotFoundException {
        //Arrange
        SubsidiaryStockRequestDTO subsidiaryStockRequestDTO = new SubsidiaryStockRequestDTO();

        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setCountry("Argentina");
        subsidiary.setName("Mercedez Argentina");
        subsidiary.setName("Calle falsa 1234");
        List<SubsidiaryStock> subsidiaryStocks = new ArrayList<>();
        subsidiary.setSubsidiaryStocks(subsidiaryStocks);

        SubsidiaryResponseDTO SubsidiaryResponseDTO = new SubsidiaryResponseDTO();
        List<OrderDTO> ordersDTO = new ArrayList<>();
        SubsidiaryResponseDTO.setDealerNumber("1");
        SubsidiaryResponseDTO.setOrders(ordersDTO);

        SubsidiaryStockResponseDTO expected = new SubsidiaryStockResponseDTO();
        List<SubsidiaryStockDTO> subsidiaryStock = new ArrayList<>();
        expected.setName("Calle falsa 1234");
        expected.setDealerNumber("");
        expected.setSubsidiaryStocks(subsidiaryStock);

        //Act
        when(subsidiaryRepository.findById(subsidiaryStockRequestDTO.getDealerNumber())).thenReturn(Optional.of(subsidiary));
        when(mapper.toOrderDTO(subsidiary)).thenReturn(SubsidiaryResponseDTO);

        SubsidiaryStockResponseDTO actual = warehouseService.findSubsidiaryStock(subsidiaryStockRequestDTO);

        //Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findSubsidiaryStockWithException() {
        //Arrange
        SubsidiaryStockRequestDTO subsidiaryStockRequestDTO = new SubsidiaryStockRequestDTO();

        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setCountry("Argentina");
        subsidiary.setName("Mercedez Argentina");
        subsidiary.setName("Calle falsa 1234");
        List<SubsidiaryStock> subsidiaryStocks = new ArrayList<>();
        subsidiary.setSubsidiaryStocks(subsidiaryStocks);

        SubsidiaryResponseDTO SubsidiaryResponseDTO = new SubsidiaryResponseDTO();
        List<OrderDTO> ordersDTO = new ArrayList<>();
        SubsidiaryResponseDTO.setDealerNumber("1");
        SubsidiaryResponseDTO.setOrders(ordersDTO);

        //Act
        when(subsidiaryRepository.findById(subsidiaryStockRequestDTO.getDealerNumber())).thenReturn(Optional.empty());
        when(mapper.toOrderDTO(subsidiary)).thenReturn(SubsidiaryResponseDTO);

        Assertions.assertThrows(SubsidiaryNotFoundException.class, () -> warehouseService.findSubsidiaryStock(subsidiaryStockRequestDTO));
    }

    @Test
    public void cancelDeliveryStatusTest() {
        //Arrange
        Subsidiary subsidiary = new Subsidiary();
        Order order = new Order();
        order.setIdOrder(1L);
        order.setSubsidiary(subsidiary);
        order.setOrderDate(new Date());
        order.setDeliveredDate(new Date());
        order.setDeliveryStatus("P");
        Part part = new Part();
        part.setPartCode(11111112);
        part.setQuantity(30);
        part.setDescription("Amortiguador delantero derecho - BMW 220i");
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPartOrder(part);
        orderDetail.setQuantity(20);
        orderDetail.setIdOrderDetail(18L);
        List<OrderDetail> orderList = new ArrayList<>();
        orderList.add(orderDetail);
        order.setOrderDetails(orderList);

        Order expect = new Order();
        expect.setIdOrder(1L);
        expect.setSubsidiary(subsidiary);
        expect.setOrderDate(new Date());
        expect.setDeliveredDate(new Date());
        expect.setDeliveryStatus("C");
        expect.setOrderDetails(orderList);

        //Act
        when(partRepository.findById(orderDetail.getPartOrder().getIdPart())).thenReturn(Optional.of(part));
        when(partRepository.save(part)).thenReturn(part);
        when(orderRepository.save(order)).thenReturn(order);

        warehouseService.cancelDeliveryStatus(order);

        //Assert
        Assertions.assertEquals(expect, order);
    }

    @Test
    public void changeDeliveryStatusToCancel() throws InternalExceptionHandler {
        //Arrange
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setCountry("Argentina");
        subsidiary.setName("Mercedez Argentina");
        subsidiary.setName("Calle falsa 1234");
        List<SubsidiaryStock> subsidiaryStocks = new ArrayList<>();
        subsidiary.setSubsidiaryStocks(subsidiaryStocks);

        Order order = new Order();
        order.setIdOrder(1L);
        order.setSubsidiary(subsidiary);
        order.setOrderDate(new Date());
        order.setDeliveredDate(new Date());
        order.setDeliveryDate(new Date());
        order.setDeliveryStatus("P");
        Part part = new Part();
        part.setPartCode(11111112);
        part.setQuantity(30);
        part.setDescription("Amortiguador delantero derecho - BMW 220i");
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPartOrder(part);
        orderDetail.setQuantity(20);
        orderDetail.setIdOrderDetail(18L);
        List<OrderDetail> orderList = new ArrayList<>();
        orderList.add(orderDetail);
        order.setOrderDetails(orderList);

        Order expect = new Order();
        expect.setIdOrder(1L);
        expect.setDeliveryDate(new Date());
        expect.setSubsidiary(subsidiary);
        expect.setOrderDate(new Date());
        expect.setDeliveredDate(new Date());
        expect.setDeliveryStatus("C");
        expect.setOrderDetails(orderList);

        //Act
        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdOrderAndSubsidiary(1L, subsidiary)).thenReturn(Optional.of(order));
        when(partRepository.findById(orderDetail.getPartOrder().getIdPart())).thenReturn(Optional.of(part));
        when(partRepository.save(part)).thenReturn(part);
        when(orderRepository.save(order)).thenReturn(order);

        warehouseService.changeDeliveryStatus("0001-00000001", "C");

        //Assert
        Assertions.assertEquals(expect, order);
    }

    @Test
    public void changeDeliveryStatusToFinish() throws InternalExceptionHandler {
        //Arrange
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setCountry("Argentina");
        subsidiary.setName("Mercedez Argentina");
        subsidiary.setName("Calle falsa 1234");
        List<SubsidiaryStock> subsidiaryStocks = new ArrayList<>();
        subsidiary.setSubsidiaryStocks(subsidiaryStocks);

        Date currentDate = new Date();
        Date deliveredDate = new Date();
        Order order = new Order();
        order.setIdOrder(1L);
        order.setSubsidiary(subsidiary);
        order.setOrderDate(currentDate);
        order.setDeliveredDate(deliveredDate);
        order.setDeliveryStatus("D");
        Part part = new Part();
        part.setPartCode(11111112);
        part.setQuantity(30);
        part.setDescription("Amortiguador delantero derecho - BMW 220i");
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPartOrder(part);
        orderDetail.setQuantity(20);
        orderDetail.setIdOrderDetail(18L);
        List<OrderDetail> orderList = new ArrayList<>();
        orderList.add(orderDetail);
        order.setOrderDetails(orderList);

        Order expect = new Order();
        expect.setIdOrder(1L);
        expect.setSubsidiary(subsidiary);
        expect.setOrderDate(currentDate);
        expect.setDeliveredDate(deliveredDate);
        expect.setDeliveryStatus("F");
        expect.setOrderDetails(orderList);

        //Act
        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdOrderAndSubsidiary(1L, subsidiary)).thenReturn(Optional.of(order));
        when(partRepository.findById(orderDetail.getPartOrder().getIdPart())).thenReturn(Optional.of(part));
        when(partRepository.save(part)).thenReturn(part);
        when(orderRepository.save(order)).thenReturn(order);

        warehouseService.changeDeliveryStatus("0001-00000001", "F");

        //Assert
        Assertions.assertEquals(expect, order);
    }

    @Test
    public void changeDeliveryStatusIsconcluded() {
        //Arrange
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setCountry("Argentina");
        subsidiary.setName("Mercedez Argentina");
        subsidiary.setName("Calle falsa 1234");
        List<SubsidiaryStock> subsidiaryStocks = new ArrayList<>();
        subsidiary.setSubsidiaryStocks(subsidiaryStocks);

        Order order = new Order();
        order.setIdOrder(1L);
        order.setSubsidiary(subsidiary);
        order.setOrderDate(new Date());
        order.setDeliveryStatus("F");
        Part part = new Part();
        part.setPartCode(11111112);
        part.setQuantity(30);
        part.setDescription("Amortiguador delantero derecho - BMW 220i");
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPartOrder(part);
        orderDetail.setQuantity(20);
        orderDetail.setIdOrderDetail(18L);
        List<OrderDetail> orderList = new ArrayList<>();
        orderList.add(orderDetail);
        order.setOrderDetails(orderList);

        //Act
        when(subsidiaryRepository.findById(1L)).thenReturn(Optional.of(subsidiary));
        when(orderRepository.findByIdOrderAndSubsidiary(1L, subsidiary)).thenReturn(Optional.of(order));

        //Assert
        Assertions.assertThrows(OrderDeliveryStatusIsconcludedException.class, () -> warehouseService.changeDeliveryStatus("0001-00000001", "F"));
    }

    @Test
    public void finishDeliveryStatus() {
        //Arrange
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setCountry("Argentina");
        subsidiary.setName("Mercedez Argentina");
        subsidiary.setName("Calle falsa 1234");
        List<SubsidiaryStock> subsidiaryStocks = new ArrayList<>();
        subsidiary.setSubsidiaryStocks(subsidiaryStocks);

        SubsidiaryStock subsidiaryStock = new SubsidiaryStock();

        Date currentDate = new Date();

        Order order = new Order();
        order.setIdOrder(1L);
        order.setSubsidiary(subsidiary);
        order.setOrderDate(currentDate);
        order.setDeliveryStatus("P");
        order.setDeliveredDate(new Date());
        Part part = new Part();
        part.setPartCode(11111112);
        part.setQuantity(30);
        part.setDescription("Amortiguador delantero derecho - BMW 220i");
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPartOrder(part);
        orderDetail.setQuantity(20);
        orderDetail.setIdOrderDetail(18L);
        List<OrderDetail> orderList = new ArrayList<>();
        orderList.add(orderDetail);
        order.setOrderDetails(orderList);

        Order expect = new Order();
        expect.setIdOrder(1L);
        expect.setSubsidiary(subsidiary);
        expect.setOrderDate(currentDate);
        expect.setDeliveredDate(new Date());
        expect.setDeliveryStatus("F");
        expect.setOrderDetails(orderList);

        //Act
        when(subsidiaryStockRepository.findByIdPart(1L, subsidiary.getIdSubsidiary())).thenReturn(subsidiaryStock);
        when(subsidiaryStockRepository.save(subsidiaryStock)).thenReturn(subsidiaryStock);

        warehouseService.finishDeliveryStatus(order, subsidiary);

        System.out.println(expect.toString());
        System.out.println(order.toString());
        //Assert
        Assertions.assertEquals(expect, order);
    }

    @Test
    public void checkDelayedOrdersWorks() {
        SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");

        List<Order> orders = new ArrayList<>();
        Subsidiary subsidiary = new Subsidiary();
        subsidiary.setIdSubsidiary(1L);
        Order o1 = new Order();
        Order o2 = new Order();

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, 3);

        o1.setIdOrder(1L);
        o1.setOrderDate(currentDate);
        o1.setDeliveryDate(calendar.getTime());
        o1.setSubsidiary(subsidiary);
        o1.setDeliveryStatus(DeliveryStatus.PENDING);
        o1.setOrderDetails(new ArrayList<>());

        calendar.add(Calendar.DAY_OF_YEAR, -6);

        o2.setIdOrder(2L);
        o2.setOrderDate(calendar.getTime());
        o2.setSubsidiary(subsidiary);
        o2.setDeliveryStatus(DeliveryStatus.PENDING);
        o2.setOrderDetails(new ArrayList<>());

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        o2.setDeliveryDate(calendar.getTime());

        orders.add(o1);
        orders.add(o2);

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderRepository.save(o1)).thenReturn(o1);
        when(orderRepository.save(o2)).thenReturn(o2);

        List<OrderDTO> result = warehouseService.checkDelayedOrders();

        OrderDTO expected = new OrderDTO();
        expected.setOrderDate(datePattern.format(o2.getOrderDate()));
        expected.setDeliveryDate(datePattern.format(o2.getDeliveryDate()));
        expected.setOrderNumberCM("0001-00000002");
        expected.setDaysDelayed(2);
        expected.setOrderDetails(new ArrayList<>());
        expected.setDeliveryStatus(DeliveryStatus.DELAYED);

        Assertions.assertEquals(expected, result.get(0));
    }
}
