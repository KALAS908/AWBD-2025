package com.example.demo.Controller;
import com.example.demo.Models.CartItem;
import com.example.demo.dto.cart.AddToCartRequest;
import com.example.demo.dto.cart.CartResponseDto;
import com.example.demo.dto.cart.RemoveFromCartRequest;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponseDto>> getCart(@PathVariable UUID userId) {
    List<CartItem> cartItems = cartService.getCart(userId);
    List<CartResponseDto> cartResponseDtos = cartItems.stream()
                .map(CartItemMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cartResponseDtos);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {
        cartService.addToCart(request.getUserId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestBody RemoveFromCartRequest request
                                           ) {
        cartService.removeFromCart(request.getUserId(), request.getProductId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestParam UUID userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
}
