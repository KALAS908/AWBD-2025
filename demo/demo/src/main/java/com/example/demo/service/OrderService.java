package com.example.demo.service;

import com.example.demo.Models.Enums.OrderStatus;
import com.example.demo.Models.Order;
import com.example.demo.Models.Product;
import com.example.demo.Models.User;
import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.product.ProductResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Create
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        User user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Product> products = orderRequestDto.getProductIds().stream()
                .map(id -> productRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id)))
                .collect(Collectors.toSet());

        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(calculateTotalPrice(products));

        Order savedOrder = orderRepository.save(order);
        return mapToDto(savedOrder);
    }

    // Read
    public OrderResponseDto getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapToDto(order);
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update
    public OrderResponseDto updateOrder(UUID id, OrderRequestDto orderRequestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (orderRequestDto.getUserId() != null) {
            User user = userRepository.findById(orderRequestDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            order.setUser(user);
        }

        if (orderRequestDto.getStatus() != null) {
            order.setStatus(orderRequestDto.getStatus());
        }

        Order updatedOrder = orderRepository.save(order);
        return mapToDto(updatedOrder);
    }

    // Delete
    public void deleteOrder(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found");
        }
        orderRepository.deleteById(id);
    }

    // Add products to order
    public OrderResponseDto addProductsToOrder(UUID orderId, Set<UUID> productIds) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Set<Product> productsToAdd = productIds.stream()
                .map(id -> productRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id)))
                .collect(Collectors.toSet());

        order.getProducts().addAll(productsToAdd);
        order.setTotalPrice(calculateTotalPrice(order.getProducts()));

        Order updatedOrder = orderRepository.save(order);
        return mapToDto(updatedOrder);
    }

    // Helper methods
    private Double calculateTotalPrice(Set<Product> products) {
        return products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    private OrderResponseDto mapToDto(Order order) {
        Set<ProductResponseDto> productDtos = order.getProducts().stream()
                .map(this::mapToProductDto)
                .collect(Collectors.toSet());

        return new OrderResponseDto(
                order.getId(),
                order.getUser().getId(),
                productDtos,
                order.getTotalPrice(),
                order.getStatus()
        );
    }

    private ProductResponseDto mapToProductDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getPhoneModel(),
                product.getCategory(),
                null
        );
    }
}

