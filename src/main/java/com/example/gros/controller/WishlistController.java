package com.example.gros.controller;

import com.example.gros.dto.ApiResponse;
import com.example.gros.dto.WishlistDTO;
import com.example.gros.dto.WishlistRequest;
import com.example.gros.model.Wishlist;
import com.example.gros.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<List<WishlistDTO>> getUserWishlist(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        List<Wishlist> wishlist = wishlistService.getWishlistByUserId(userId);
        List<WishlistDTO> wishlistDTOs = wishlist.stream()
                .map(WishlistDTO::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(wishlistDTOs);
    }

    @PostMapping
    public ResponseEntity<WishlistDTO> addToWishlist(
            @Valid @RequestBody WishlistRequest request,
            HttpServletRequest servletRequest) {
        
        HttpSession session = servletRequest.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        Wishlist wishlistItem = wishlistService.addToWishlist(
                userId, 
                request.getProductId(), 
                request.getQuantity());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(new WishlistDTO(wishlistItem));
    }

    @PutMapping("/{wishlistId}")
    public ResponseEntity<WishlistDTO> updateWishlistItem(
            @PathVariable Integer wishlistId,
            @Valid @RequestBody WishlistRequest request,
            HttpServletRequest servletRequest) {
        
        HttpSession session = servletRequest.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        Wishlist updatedItem = wishlistService.updateWishlistItem(
                wishlistId, 
                request.getQuantity(), 
                userId);
        
        return ResponseEntity.ok(new WishlistDTO(updatedItem));
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<ApiResponse> removeFromWishlist(
            @PathVariable Integer wishlistId,
            HttpServletRequest servletRequest) {
        
        HttpSession session = servletRequest.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        wishlistService.removeFromWishlist(wishlistId, userId);
        
        return ResponseEntity.ok(new ApiResponse(true, "Item removed from wishlist"));
    }
} 