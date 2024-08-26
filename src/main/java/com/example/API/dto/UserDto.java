package com.example.API.dto;

import com.example.API.model.Cart;

import java.util.List;

public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private Cart cart;
}
