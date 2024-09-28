package com.devtegani.orderms.entities;

import com.devtegani.orderms.listener.dto.OrderCreatedEventDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "tb_orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @MongoId
    private Long orderId;
    @Indexed(name = "customer_id_indexed")
    private Long customerId;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal total;
    private List<OrderItem> items = new ArrayList<>();

    public Order(OrderCreatedEventDTO orderCreatedEventData) {
        this.orderId = orderCreatedEventData.orderId();
        this.customerId = orderCreatedEventData.clientId();
        this.addItems(orderCreatedEventData);
        this.calculateTotal(orderCreatedEventData);
    }

    public final void addItems(OrderCreatedEventDTO orderCreatedEventData) {
        orderCreatedEventData
                .items()
                .forEach(
                        orderItemEvent -> this.items.add(
                                new OrderItem(
                                        orderItemEvent.product(),
                                        orderItemEvent.quantity(),
                                        orderItemEvent.price()
                                )
                        )
                );
    }

    public final void calculateTotal(OrderCreatedEventDTO orderCreatedEventData) {
        this.total = orderCreatedEventData
                .items()
                .stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
