package com.example.gros.service;

import com.example.gros.model.Wishlist;
import com.example.gros.model.User;
import com.example.gros.model.Product;
import com.example.gros.repository.WishlistRepository;
import com.example.gros.repository.UserRepository;
import com.example.gros.repository.ProductRepository;
import com.example.gros.exception.UserNotFoundException;
import com.example.gros.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Wishlist> getWishlistByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        
        // Verify user is a customer
        if (!"CUSTOMER".equalsIgnoreCase(user.getUserRole())) {
            throw new SecurityException("Only customers can have wishlists");
        }
        
        return wishlistRepository.findByUser(user);
    }

    @Transactional
    public Wishlist addToWishlist(Integer userId, Integer productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        
        // Verify user is a customer
        if (!"CUSTOMER".equalsIgnoreCase(user.getUserRole())) {
            throw new SecurityException("Only customers can add to wishlists");
        }
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        
        // Check if product already exists in wishlist
        if (wishlistRepository.existsByUserAndProduct(user, product)) {
            Wishlist existingItem = wishlistRepository.findByUserAndProduct(user, product)
                    .orElseThrow(() -> new RuntimeException("Unexpected error: Wishlist item not found"));
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            return wishlistRepository.save(existingItem);
        }
        
        // Create new wishlist item
        Wishlist wishlistItem = new Wishlist();
        wishlistItem.setUser(user);
        wishlistItem.setProduct(product);
        wishlistItem.setQuantity(quantity);
        
        return wishlistRepository.save(wishlistItem);
    }

    @Transactional
    public Wishlist updateWishlistItem(Integer wishlistId, Integer quantity, Integer userId) {
        Wishlist wishlistItem = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist item not found"));
        
        // Verify ownership
        if (!wishlistItem.getUser().getCustomerId().equals(userId)) {
            throw new SecurityException("You can only update your own wishlist items");
        }
        
        wishlistItem.setQuantity(quantity);
        return wishlistRepository.save(wishlistItem);
    }

    @Transactional
    public void removeFromWishlist(Integer wishlistId, Integer userId) {
        Wishlist wishlistItem = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist item not found"));
        
        // Verify ownership
        if (!wishlistItem.getUser().getCustomerId().equals(userId)) {
            throw new SecurityException("You can only remove your own wishlist items");
        }
        
        wishlistRepository.delete(wishlistItem);
    }
} 