package com.devtegani.orderms.listener;

import com.devtegani.orderms.factory.OrderCreatedEventDTOFactory;
import com.devtegani.orderms.listener.dto.OrderCreatedEventDTO;
import com.devtegani.orderms.services.OrderService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderCreatedListenerTest {
    @Mock
    OrderService orderService;

    @InjectMocks
    OrderCreatedListener orderCreatedListener;

    @Nested
    class Listen {
        @Test
        void shouldCallServiceWithCorrectParameters() {
//            ARRANGE
            OrderCreatedEventDTO event = OrderCreatedEventDTOFactory.buildWithOneItem();
            Message<OrderCreatedEventDTO> message = MessageBuilder.withPayload(event).build();

//            ACT
            orderCreatedListener.listen(message);
//            ASSERT
            verify(orderService, times(1)).save(eq(message.getPayload()));
        }

    }
}