package com.uom.supermarketbackend.controller;

import com.uom.supermarketbackend.dto.OrderDTO;
import com.uom.supermarketbackend.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) throws Exception {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PatchMapping("/update/{orderId}")
    @PreAuthorize("hasAuthority('INVENTORY_KEEPER')")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, orderDTO.getOrderStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/get/{orderId}")

    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Iterable<OrderDTO>> getAllOrders() {
        Iterable<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId) {
        OrderDTO canceledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(canceledOrder);
    }

    @DeleteMapping("/delete/{orderId}")
    @PreAuthorize("hasAuthority('INVENTORY_KEEPER')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
