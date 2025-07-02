package com.example.gros.dto;

import com.example.gros.model.User;

public class UserDTO {
    private Integer customerId;
    private String customerName;
    private String email;
    private String address;
    private Long contactNumber;
    private String userRole;
    
    public UserDTO() {
    }
    
    public UserDTO(User user) {
        this.customerId = user.getCustomerId();
        this.customerName = user.getCustomerName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.contactNumber = user.getContactNumber();
        this.userRole = user.getUserRole();
    }
    
    // Getters and setters
    public Integer getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Long getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public String getUserRole() {
        return userRole;
    }
    
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
} 