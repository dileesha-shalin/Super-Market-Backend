package com.uom.supermarketbackend.service;

import com.uom.supermarketbackend.dto.DeliveryDTO;
import com.uom.supermarketbackend.dto.DeliveryUpdateDTO;
import com.uom.supermarketbackend.model.Delivery;
import com.uom.supermarketbackend.model.Order;
import com.uom.supermarketbackend.model.User;
import com.uom.supermarketbackend.repository.DeliveryRepository;
import com.uom.supermarketbackend.repository.OrderRepository;
import com.uom.supermarketbackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;

    private final OrderRepository orderRepository;

    public DeliveryDTO createDelivery(DeliveryDTO deliveryDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByEmail(currentPrincipalName)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        Order order = orderRepository.findById(deliveryDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        User deliveryPerson = userRepository.findDeliveryPersonById(deliveryDTO.getDeliverPersonId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        Delivery delivery= Delivery.builder()
                .order(order)
                .trackingId(deliveryDTO.getTrackingId())
                .shippingAddress(deliveryDTO.getShippingAddress())
                .deliverDate(deliveryDTO.getDeliverDate())
                .deliveryStatus("On the Way")
                .deliveryPerson(deliveryPerson)
                .user(user)
                .build();

        deliveryRepository.save(delivery);
        return deliverToDeliverDTO(delivery);
    }

    public DeliveryDTO updateDeliveryStatus(DeliveryUpdateDTO deliveryUpdateDTO) {
        Long deliveryId = deliveryUpdateDTO.getDeliveryId();
        String newStatus = deliveryUpdateDTO.getNewStatus();

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found"));

        delivery.setDeliveryStatus(newStatus);

        Order order = delivery.getOrder();
        if (order != null) {
            order.setDeliveryStatus(newStatus);
            orderRepository.save(order);
        }

        deliveryRepository.save(delivery);

        return deliverToDeliverDTO(delivery);
    }

    public DeliveryDTO deliverToDeliverDTO(Delivery delivery){
        return DeliveryDTO.builder()
                .trackingId(delivery.getTrackingId())
                .deliveryStatus(delivery.getDeliveryStatus())
                .deliverDate(delivery.getDeliverDate())
                .orderId(delivery.getOrder().getOrderId())
                .shippingAddress(delivery.getShippingAddress())
                .deliverPersonId(delivery.getDeliveryPerson().getId())
                .build();
    }
}
