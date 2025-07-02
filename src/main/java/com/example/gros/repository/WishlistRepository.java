package com.example.gros.repository;

import com.example.gros.model.Wishlist;
import com.example.gros.model.User;
import com.example.gros.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    List<Wishlist> findByUser(User user);
    List<Wishlist> findByUserId(Integer userId);
    Optional<Wishlist> findByUserAndProduct(User user, Product product);
    boolean existsByUserAndProduct(User user, Product product);
} 