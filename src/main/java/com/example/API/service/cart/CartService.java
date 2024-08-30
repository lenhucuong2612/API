package com.example.API.service.cart;

import com.example.API.exception.ResourceNotFoundException;
import com.example.API.model.Cart;
import com.example.API.model.CartItem;
import com.example.API.model.User;
import com.example.API.repository.CartItemRepository;
import com.example.API.repository.CartRepository;
import com.example.API.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final AtomicLong cartIdGenerator=new AtomicLong(0);
    @Override
    public Cart getCart(Long id) {
        Cart cart=cartRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount=cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart=getCart(id);
        cartItemRepository.deleteAllCartById(id);
        cartItemRepository.flush();
        if(cart.getUser()!=null){
            cart.getUser().setCart(null);
        }
        cart.getItems().clear();
        cartRepository.deleteById(id);
        cartRepository.flush();
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart=getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(User user){
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()->{
                   Cart cart =new Cart();
                   cart.setUser(user);
                   return cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
