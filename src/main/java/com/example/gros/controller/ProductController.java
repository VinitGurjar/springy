package com.example.gros.controller;

import com.example.gros.dto.ApiResponse;
import com.example.gros.dto.ProductDTO;
import com.example.gros.model.Product;
import com.example.gros.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getProducts(@RequestParam(required = false) Integer id,
                                         @RequestParam(required = false) String name) {
        if (id != null) {
            Optional<Product> product = productService.getProductById(id);
            return product.<ResponseEntity<?>>map(p -> ResponseEntity.ok(new ProductDTO(p)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse(false, "Product not found")));
        } else if (name != null) {
            List<Product> products = productService.searchProductsByName(name);
            List<ProductDTO> productDTOs = products.stream()
                    .map(ProductDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(productDTOs);
        } else {
            List<Product> products = productService.getAllProducts();
            List<ProductDTO> productDTOs = products.stream()
                    .map(ProductDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(productDTOs);
        }
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product,
                                        HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("userRole"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "Only admin can add products"));
        }
        
        Integer adminId = (Integer) session.getAttribute("userId");
        Product created = productService.addProduct(product, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductDTO(created));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer productId,
                                           @Valid @RequestBody Product product,
                                           HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("userRole"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "Only admin can update products"));
        }
        
        Integer adminId = (Integer) session.getAttribute("userId");
        Product updated = productService.updateProduct(productId, product, adminId);
        return ResponseEntity.ok(new ProductDTO(updated));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId,
                                           HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("userRole"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "Only admin can delete products"));
        }
        
        Integer adminId = (Integer) session.getAttribute("userId");
        productService.deleteProduct(productId, adminId);
        return ResponseEntity.ok(new ApiResponse(true, "Product deleted successfully"));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String name) {
        List<Product> products = productService.searchProductsByName(name);
        List<ProductDTO> productDTOs = products.stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }
} 