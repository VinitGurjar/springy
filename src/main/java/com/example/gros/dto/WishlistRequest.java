package com.example.gros.dto;

import jakarta.validation.constraints.*;

public class WishlistRequest {
    @NotNull
    private Integer productId;
    
    @NotNull
    @Min(1)
    private Integer quantity;
    
    // Getters and setters
    public Integer getProductId() {
        return productId;
    }
    
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
} 