package com.example.API.controller;

import com.example.API.exception.ResourceNotFoundException;
import com.example.API.model.Cart;
import com.example.API.model.User;
import com.example.API.response.ApiResponse;
import com.example.API.service.cart.ICartItemService;
import com.example.API.service.cart.ICartService;
import com.example.API.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long userId,
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity){
        try {
            User user=userService.getUserById(userId);
            Cart cart=cartService.initializeNewCart(user);
            cartItemService.addCartItem(cart.getId(),productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Add Items Success",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId){
        try {
            cartItemService.removeItemFromCart(cartId,itemId);
            return ResponseEntity.ok(new ApiResponse("Remove Items Success",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long itemId,
                                                          @RequestParam Integer quantity){
        try {
            cartItemService.updateItemQuantity(cartId,itemId,quantity);
            return ResponseEntity.ok(new ApiResponse("Update Item Success",null));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
