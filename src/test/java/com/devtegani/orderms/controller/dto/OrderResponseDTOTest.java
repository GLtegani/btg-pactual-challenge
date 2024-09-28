package com.devtegani.orderms.controller.dto;

import com.devtegani.orderms.entities.Order;
import com.devtegani.orderms.factory.OrderFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderResponseDTOTest {

    @Nested
    class FromEntity {
        @Test
        void shouldMapCorrectly() {
//            ARRANGE
            Order input = OrderFactory.build();
//            ACT
            OrderResponseDTO output = OrderResponseDTO.fromEntity(input);

//            ASSERT
            assertEquals(input.getOrderId(), output.orderId());
            assertEquals(input.getCustomerId(), output.clientId());
            assertEquals(input.getTotal(), output.total());
        }
    }
}