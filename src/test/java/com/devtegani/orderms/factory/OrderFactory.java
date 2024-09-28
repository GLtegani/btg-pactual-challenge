package com.devtegani.orderms.factory;

import com.devtegani.orderms.entities.Order;
import com.devtegani.orderms.entities.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

public class OrderFactory {
    public static Order build() {
        OrderItem items = new OrderItem("notebook", 1, BigDecimal.valueOf(20.50));
        Order entity = new Order();
        entity.setOrderId(1L);
        entity.setCustomerId(2L);
        entity.setTotal(BigDecimal.valueOf(20.50));
        entity.setItems(List.of(items));
        return entity;
    }

    public static Page<Order> buildWithPage() {
        return new PageImpl<>(List.of(build()));
    }
}
