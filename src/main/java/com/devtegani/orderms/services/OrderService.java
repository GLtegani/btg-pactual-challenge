package com.devtegani.orderms.services;

import com.devtegani.orderms.controller.dto.OrderResponseDTO;
import com.devtegani.orderms.entities.Order;
import com.devtegani.orderms.listener.dto.OrderCreatedEventDTO;
import com.devtegani.orderms.repositories.OrderRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public final void save(OrderCreatedEventDTO event) {
        Order order = new Order(event);

        this.orderRepository.save(order);
    }

    public final Page<OrderResponseDTO> findAllByCostumerId(Long customerId, PageRequest pageRequest) {
        Page<Order> orders = this.orderRepository.findAllByCustomerId(customerId, pageRequest);

        return orders.map(OrderResponseDTO::fromEntity);
    }

    public final BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        Aggregation aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );

        AggregationResults<Document> response = this.mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);

        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }
}
