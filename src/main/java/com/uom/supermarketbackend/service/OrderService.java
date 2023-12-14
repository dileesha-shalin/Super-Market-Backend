package com.uom.supermarketbackend.service;

import com.uom.supermarketbackend.dto.OrderDTO;
import com.uom.supermarketbackend.dto.OrderItemDTO;
import com.uom.supermarketbackend.model.Order;
import com.uom.supermarketbackend.model.OrderItem;
import com.uom.supermarketbackend.model.Product;
import com.uom.supermarketbackend.model.User;
import com.uom.supermarketbackend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByEmail(currentPrincipalName)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        List<OrderItemDTO> orderProductDTOList = orderDTO.getOrderedProducts();

        for (OrderItemDTO itemProduct : orderProductDTOList) {
            Product product = productRepository.findById(itemProduct.getId()).orElse(null);
            if (product != null) {
                if (product.getStockQuantity() <= itemProduct.getQuantity()) {
                    throw new Exception(" is currently out of stock");
                } else {
                    int newQuantity = product.getStockQuantity() - itemProduct.getQuantity();
                    product.setStockQuantity(newQuantity);
                }

            } else {
                throw new IllegalArgumentException("Product not Found");
            }
        }

        Order order= Order.builder()
                .orderDate(new Date().toString())
                .totalQuantity(orderDTO.getTotalQuantity())
                .totalPrice(orderDTO.getTotalPrice())
                .orderStatus("processing")
                .deliveryStatus("pending")
                .user(user)
                .build();


        List<OrderItem> orderProducts = new ArrayList<>();

        //setting products in OrderProduct entity
        for (OrderItemDTO prodOrder : orderProductDTOList) {
            Product product = productRepository.findById(prodOrder.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product Not Found"));

            OrderItem orderProduct = OrderItem.builder()
                    .prodQuantity(prodOrder.getQuantity())
                    .product(product)
                    .order(order)
                    .build();

            //add product to arraylist
            orderProducts.add(orderProduct);
        }

        order.setOrderedProducts(orderProducts);
        orderRepository.save(order);

        return orderToOrderDTO(order);
    }

    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        // Update order status
        order.setOrderStatus(newStatus);
        orderRepository.save(order);

        return orderToOrderDTO(order);
    }

    //cancel order by customer
    public void deleteOrder(Long orderId) {
        orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        orderRepository.deleteById(orderId);
    }

    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setOrderStatus("canceled");
        return orderToOrderDTO(order);
    }
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return orderToOrderDTO(order);
    }

    public Iterable<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::orderToOrderDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO orderToOrderDTO(Order order) {
        List<OrderItem> orderProducts = order.getOrderedProducts();
        List<OrderItemDTO> orderProductDTOs = orderProducts.stream()
                .map(this::orderProductToOrderProductDTO)
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .totalQuantity(order.getTotalQuantity())
                .totalPrice(order.getTotalPrice())
                .orderDate(new Date().toString())
                .orderStatus(order.getOrderStatus())
                .deliveryStatus(order.getDeliveryStatus())
                .orderedProducts(orderProductDTOs)
                .build();
    }

    private OrderItemDTO orderProductToOrderProductDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getProdQuantity())
                .build();
    }

}