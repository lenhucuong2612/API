package com.example.API.dto;

import com.example.API.model.Order;
import com.example.API.model.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;

}
