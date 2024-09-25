package com.devtegani.orderms.listener.dto;

import java.util.List;

public record OrderCreatedEventDTO(
        Long orderId,
        Long clientId,
        List<OrderItemEventDTO> items
) {
}

