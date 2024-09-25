package com.devtegani.orderms.listener.dto;

import java.math.BigDecimal;

public record OrderItemEventDTO(
        String product,
        Integer quantity,
        BigDecimal price
) {
}

