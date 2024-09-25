package com.devtegani.orderms.listener;

import com.devtegani.orderms.listener.dto.OrderCreatedEventDTO;
import com.devtegani.orderms.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.devtegani.orderms.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Component
public class OrderCreatedListener {
    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);
    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public final void listen(Message<OrderCreatedEventDTO> message) {
        this.logger.info("Message consumed: {}", message);

        this.orderService.save(message.getPayload());
    }
}
