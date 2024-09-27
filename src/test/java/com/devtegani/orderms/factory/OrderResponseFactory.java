package com.devtegani.orderms.factory;

import com.devtegani.orderms.controller.dto.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponseFactory {
    public static Page<OrderResponseDTO> buildWithOneItem() {
        OrderResponseDTO orderResponse = new OrderResponseDTO(1L, 2L, BigDecimal.valueOf(20.50));

        return new PageImpl<>(List.of(orderResponse));
    }
}
