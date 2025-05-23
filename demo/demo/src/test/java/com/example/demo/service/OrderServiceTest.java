package com.example.demo.service;

import com.example.demo.Models.Enums.OrderStatus;
import com.example.demo.Models.Order;
import com.example.demo.Models.Product;
import com.example.demo.Models.User;
import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private UUID orderId;
    private UUID userId;
    private UUID productId;
    private User user;
    private Product product;
    private Order order;
    private OrderRequestDto requestDto;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        userId = UUID.randomUUID();
        productId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setFirstName("John");

        product = new Product();
        product.setId(productId);
        product.setName("iPhone");
        product.setPrice(999.99);

        order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setProducts(new HashSet<>(Arrays.asList(product)));
        order.setTotalPrice(999.99);
        order.setStatus(OrderStatus.PENDING);

        requestDto = new OrderRequestDto();
        requestDto.setUserId(userId);
        requestDto.setProductIds(Set.of(productId));
        requestDto.setStatus(OrderStatus.PENDING);
    }


    @Test
    void getOrderById_WhenExists_ShouldReturnOrder() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderResponseDto result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(999.99, result.getTotalPrice());
    }

    private Product createProduct(double price) {
        Product p = new Product();
        p.setId(UUID.randomUUID());
        p.setPrice(price);
        return p;
    }
}

