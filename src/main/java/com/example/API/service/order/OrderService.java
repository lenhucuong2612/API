package com.example.API.service.order;

import com.example.API.dto.OrderDto;
import com.example.API.enums.OrderStatus;
import com.example.API.exception.ResourceNotFoundException;
import com.example.API.model.Cart;
import com.example.API.model.Order;
import com.example.API.model.OrderItem;
import com.example.API.model.Product;
import com.example.API.repository.OrderRepository;
import com.example.API.repository.ProductRepository;
import com.example.API.service.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart=cartService.getCartByUserId(userId);
        Order order=createOrder(cart);
        List<OrderItem> orderItemList=createOrderItems(order,cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder=orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart){
        Order order=new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }
    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem -> {
            Product product=cartItem.getProduct();
            product.setInventory(product.getInventory()-cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
        }).toList();
    }
    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList.stream().map(item->item.getPrice().multiply(
                new BigDecimal(item.getQuantity())
        )).reduce(BigDecimal.ZERO,BigDecimal::add);
    }
    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(()->new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders=orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order,OrderDto.class);
    }
}
