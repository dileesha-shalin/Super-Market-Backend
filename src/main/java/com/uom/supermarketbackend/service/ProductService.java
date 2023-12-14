package com.uom.supermarketbackend.service;

import com.uom.supermarketbackend.dto.ProductDTO;
import com.uom.supermarketbackend.model.Product;
import com.uom.supermarketbackend.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> new ProductDTO(product))
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(product1 -> new ProductDTO(product1)).orElse(null);
    }

    public List<ProductDTO> searchProducts(String productName) {
        List<Product> products = productRepository.findAllByNameContainingIgnoreCase(productName);
        return products.stream()
                .map(product -> new ProductDTO(product))
                .collect(Collectors.toList());
    }

    public Product saveProducts(Product product) {
        return productRepository.save(product);
    }

    public ProductDTO updateProduct(Long productId, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setDescription(updatedProduct.getDescription());
            product.setStockQuantity(updatedProduct.getStockQuantity());

            productRepository.save(product);
            return new ProductDTO(product);
        }
        return null;
    }

    public List<ProductDTO> deleteProduct(Long productId) {
        productRepository.deleteById(productId);
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> new ProductDTO(product))
                .collect(Collectors.toList());
    }

}