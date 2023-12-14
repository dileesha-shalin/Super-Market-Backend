package com.uom.supermarketbackend.dto;

import com.uom.supermarketbackend.enums.DeliveryStatus;
import com.uom.supermarketbackend.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int totalQuantity;
    private long totalPrice;
    private String orderDate;
    private String orderStatus;
    private String deliveryStatus;
    private List<OrderItemDTO> orderedProducts;
}