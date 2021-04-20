package com.bootcamp.finalProject.utils;

import com.bootcamp.finalProject.dtos.OrderDTO;
import com.bootcamp.finalProject.dtos.OrderResponseDTO;
import com.bootcamp.finalProject.mnemonics.DeliveryStatus;
import com.bootcamp.finalProject.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bootcamp.finalProject.utils.MapperUtils.completeNumberByLength;
import static com.bootcamp.finalProject.utils.MapperUtils.getDifferencesInDays;

public class OrderResponseMapper {
    OrderDetailResponseMapper mapper = new OrderDetailResponseMapper();
    SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");

    public OrderDTO toDTO(Order order, Long idSubsidiary) {
        OrderDTO ret = new OrderDTO();

        ret.setOrderNumberCM(completeNumberByLength(String.valueOf(idSubsidiary), 4) + "-" + completeNumberByLength(String.valueOf(order.getIdOrder()), 8));
        ret.setOrderDate(datePattern.format(order.getOrderDate()));
        ret.setDeliveryDate(datePattern.format(order.getDeliveryDate()));
        if (order.getDeliveryStatus().equals(DeliveryStatus.CANCELED)) {
            ret.setDaysDelayed(0);
        } else {
            ret.setDaysDelayed(getDifferencesInDays(order.getDeliveryDate(), order.getDeliveredDate()));
        }
        ret.setDeliveryStatus(order.getDeliveryStatus());
        ret.setOrderDetails(order.getOrderDetails().stream().map(f -> mapper.toDTO(f)).collect(Collectors.toList()));

        return ret;
    }

    public OrderResponseDTO toOrderNumberCMDTO(Order order, Long idSubsidiary) {
        OrderResponseDTO a = new OrderResponseDTO();

        a.setOrderNumberCM(completeNumberByLength(String.valueOf(idSubsidiary), 4) + "-" + completeNumberByLength(String.valueOf(order.getIdOrder()), 8));
        a.setOrderDate(datePattern.format(order.getOrderDate()));
        a.setDeliveryStatus(order.getDeliveryStatus());
        a.setOrderDetails(order.getOrderDetails().stream().map(f -> mapper.toDTO(f)).collect(Collectors.toList()));
        return a;
    }

    public List<OrderDTO> orderDTOList(List<Order> orders) {
        List<OrderDTO> result = new ArrayList<>();
        for (Order o : orders) {
            OrderDTO dto = new OrderDTO();
            dto.setOrderNumberCM(completeNumberByLength(String.valueOf(o.getSubsidiary().getIdSubsidiary()), 4)
                    + "-" + completeNumberByLength(String.valueOf(o.getIdOrder()), 8));
            dto.setOrderDate(datePattern.format(o.getOrderDate()));
            dto.setDeliveryDate(datePattern.format(o.getDeliveryDate()));
            dto.setDaysDelayed(getDifferencesInDays(o.getDeliveryDate(), o.getDeliveredDate()));
            dto.setDeliveryStatus(o.getDeliveryStatus());
            dto.setOrderDetails(o.getOrderDetails().stream().map(f -> mapper.toDTO(f)).collect(Collectors.toList()));

            result.add(dto);
        }

        return result;
    }
}
