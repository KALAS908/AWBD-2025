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
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    private User testUser;
    private Product testProduct;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setUserName("testuser");

        testProduct = new Product();
        testProduct.setId(UUID.randomUUID());
        testProduct.setName("Test Product");
        testProduct.setPrice(99.99);

        testOrder = new Order();
        testOrder.setId(UUID.randomUUID());
        testOrder.setUser(testUser);
        testOrder.setProducts(Set.of(testProduct));
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setTotalPrice(99.99);
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() {
        // Arrange
        Set<UUID> productIds = Set.of(testProduct.getId());
        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(testUser.getId());
        requestDto.setProductIds(productIds);

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        OrderResponseDto result = orderService.createOrder(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(testOrder.getId(), result.getId());
        assertEquals(testUser.getId(), result.getUserId());
        assertEquals(99.99, result.getTotalPrice());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void updateOrder_ShouldReturnUpdatedOrder() {
        // Arrange
        UUID orderId = testOrder.getId();
        User newUser = new User();
        newUser.setId(UUID.randomUUID());
        newUser.setUserName("newuser");

        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(newUser.getId());
        requestDto.setStatus(OrderStatus.COMPLETED);

        Order updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setUser(newUser);
        updatedOrder.setProducts(testOrder.getProducts());
        updatedOrder.setStatus(OrderStatus.COMPLETED);
        updatedOrder.setTotalPrice(99.99);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(userRepository.findById(newUser.getId())).thenReturn(Optional.of(newUser));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // Act
        OrderResponseDto result = orderService.updateOrder(orderId, requestDto);

        // Assert
        assertEquals(newUser.getId(), result.getUserId());
        assertEquals(OrderStatus.COMPLETED, result.getStatus());
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertEquals(OrderStatus.COMPLETED, savedOrder.getStatus());
        assertEquals(newUser.getId(), savedOrder.getUser().getId());
    }

    @Test
    void updateOrder_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(testUser.getId());

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> orderService.updateOrder(orderId, requestDto));
    }

    @Test
    void deleteOrder_ShouldCallRepository() {
        // Arrange
        UUID orderId = testOrder.getId();
        when(orderRepository.existsById(orderId)).thenReturn(true);

        // Act
        orderService.deleteOrder(orderId);

        // Assert
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void deleteOrder_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderRepository.existsById(orderId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> orderService.deleteOrder(orderId));
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() {
        // Arrange
        Order order2 = new Order();
        order2.setId(UUID.randomUUID());
        order2.setUser(testUser);
        order2.setProducts(Set.of(testProduct));
        order2.setStatus(OrderStatus.COMPLETED);
        order2.setTotalPrice(199.99);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(testOrder, order2));

        // Act
        List<OrderResponseDto> result = orderService.getAllOrders();

        // Assert
        assertEquals(2, result.size());
        assertEquals(testOrder.getId(), result.get(0).getId());
        assertEquals(order2.getId(), result.get(1).getId());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void addProductsToOrder_ShouldReturnUpdatedOrder() {
        // Arrange
        UUID orderId = testOrder.getId();
        Product newProduct = new Product();
        newProduct.setId(UUID.randomUUID());
        newProduct.setName("New Product");
        newProduct.setPrice(50.0);

        Set<UUID> productIds = Set.of(newProduct.getId());

        Order updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setUser(testUser);
        updatedOrder.setProducts(Set.of(testProduct, newProduct));
        updatedOrder.setStatus(OrderStatus.PENDING);
        updatedOrder.setTotalPrice(149.99);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(productRepository.findById(newProduct.getId())).thenReturn(Optional.of(newProduct));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // Act
        OrderResponseDto result = orderService.addProductsToOrder(orderId, productIds);

        // Assert
        assertEquals(149.99, result.getTotalPrice());
        assertEquals(2, result.getProducts().size());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
