package com.uom.supermarketbackend.controller;

import com.uom.supermarketbackend.dto.ProductDTO;
import com.uom.supermarketbackend.model.Product;
import com.uom.supermarketbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('INVENTORY_KEEPER')")
    public Product getProduct(@RequestBody Product product)
    {
        return productService.saveProducts(product);
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ProductDTO getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam String name) {
        return productService.searchProducts(name);
    }

    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAuthority('INVENTORY_KEEPER')")
    public ProductDTO updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        return productService.updateProduct(productId, updatedProduct);
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAuthority('INVENTORY_KEEPER')")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }
}