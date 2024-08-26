package com.example.API.dto;

import com.example.API.enums.OrderStatus;
import com.example.API.model.OrderItem;
import com.example.API.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class OrderDto {
    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private List<OrderItemDto> items;
}
