package com.devtegani.orderms.factory;

import com.devtegani.orderms.listener.dto.OrderCreatedEventDTO;
import com.devtegani.orderms.listener.dto.OrderItemEventDTO;

import java.math.BigDecimal;
import java.util.List;

public class OrderCreatedEventDTOFactory {
    public static OrderCreatedEventDTO buildWithOneItem() {
        OrderItemEventDTO items = new OrderItemEventDTO("notebook", 1, BigDecimal.valueOf(20.50));

        return new OrderCreatedEventDTO(1L, 2L, List.of(items));
    }

    public static OrderCreatedEventDTO buildWithTwoItems() {
        OrderItemEventDTO item1 = new OrderItemEventDTO("notebook", 1, BigDecimal.valueOf(20.50));
        OrderItemEventDTO item2 = new OrderItemEventDTO("mouse", 1, BigDecimal.valueOf(35.25));

        return new OrderCreatedEventDTO(1L, 2L, List.of(item1, item2));
    }
}
