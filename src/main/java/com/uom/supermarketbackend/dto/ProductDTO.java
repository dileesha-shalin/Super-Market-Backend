package com.uom.supermarketbackend.dto;

import com.uom.supermarketbackend.model.Product;
import lombok.*;


@Data
@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private int stockQuantity;
    private String Description ;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
        this.Description=product.getDescription();
    }
}