package com.uom.supermarketbackend.controller;

import com.uom.supermarketbackend.dto.DeliveryDTO;
import com.uom.supermarketbackend.dto.DeliveryUpdateDTO;
import com.uom.supermarketbackend.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping("/create-delivery")
    @PreAuthorize("hasAuthority('INVENTORY_KEEPER')")
    public ResponseEntity<DeliveryDTO> createDelivery(@RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO createdDelivery = deliveryService.createDelivery(deliveryDTO);
        return ResponseEntity.ok(createdDelivery);
    }

    @PutMapping("/update-status")
    @PreAuthorize("hasAuthority('DELIVERY_PERSON')")
    public ResponseEntity<DeliveryDTO> updateDeliveryStatus(
            @RequestBody DeliveryUpdateDTO deliveryUpdateDTO){
        DeliveryDTO updatedDelivery = deliveryService.updateDeliveryStatus(deliveryUpdateDTO);
        return ResponseEntity.ok(updatedDelivery);
    }
}
