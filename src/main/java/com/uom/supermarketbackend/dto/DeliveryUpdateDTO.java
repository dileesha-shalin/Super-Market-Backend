package com.uom.supermarketbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class DeliveryUpdateDTO {
    private Long deliveryId;      // ID of the delivery to be updated
    private String newStatus;     // The new delivery status to set
}
