package com.uom.supermarketbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class DeliveryDTO {
    private Long orderId;
    private String trackingId;
    private Long deliverPersonId;
    private String shippingAddress;
    private String deliveryStatus;
    private String deliverDate;
}