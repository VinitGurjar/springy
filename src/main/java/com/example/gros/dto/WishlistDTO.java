package com.example.gros.dto;

import com.example.gros.model.Wishlist;

public class WishlistDTO {
    private Integer wishlistId;
    private Integer userId;
    private Integer productId;
    private String productName;
    private Double productPrice;
    private String productDescription;
    private Integer quantity;
    
    public WishlistDTO() {
    }
    
    public WishlistDTO(Wishlist wishlist) {
        this.wishlistId = wishlist.getWishlistId();
        this.userId = wishlist.getUser().getCustomerId();
        this.productId = wishlist.getProduct().getProductId();
        this.productName = wishlist.getProduct().getProductName();
        this.productPrice = wishlist.getProduct().getPrice();
        this.productDescription = wishlist.getProduct().getProductDescription();
        this.quantity = wishlist.getQuantity();
    }
    
    // Getters and setters
    public Integer getWishlistId() {
        return wishlistId;
    }
    
    public void setWishlistId(Integer wishlistId) {
        this.wishlistId = wishlistId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getProductId() {
        return productId;
    }
    
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public Double getProductPrice() {
        return productPrice;
    }
    
    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
} 