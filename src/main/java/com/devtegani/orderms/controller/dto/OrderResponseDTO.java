package com.devtegani.orderms.controller.dto;

import com.devtegani.orderms.entities.Order;

import java.math.BigDecimal;

public record OrderResponseDTO(Long orderId, Long clientId, BigDecimal total) {
    public static OrderResponseDTO fromEntity(Order order) {
        return new OrderResponseDTO(order.getOrderId(), order.getCustomerId(), order.getTotal());
    }
}
