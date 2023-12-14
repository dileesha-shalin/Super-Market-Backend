package com.uom.supermarketbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "t_order_product")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_orderId")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "fk_prodId")
    @JsonBackReference
    private Product product;

    private Integer prodQuantity;
}