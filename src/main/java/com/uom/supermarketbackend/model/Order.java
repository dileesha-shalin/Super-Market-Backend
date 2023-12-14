package com.uom.supermarketbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private int totalQuantity;
    private Long totalPrice;
    private String orderDate;

    private String orderStatus;

    private String deliveryStatus;

    @ManyToOne
    @JoinColumn(name = "fk_d", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderedProducts;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Delivery delivery;
}
