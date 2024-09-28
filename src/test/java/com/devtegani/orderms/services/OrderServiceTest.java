package com.devtegani.orderms.services;

import com.devtegani.orderms.controller.dto.OrderResponseDTO;
import com.devtegani.orderms.entities.Order;
import com.devtegani.orderms.factory.OrderCreatedEventDTOFactory;
import com.devtegani.orderms.factory.OrderFactory;
import com.devtegani.orderms.listener.dto.OrderCreatedEventDTO;
import com.devtegani.orderms.repositories.OrderRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;

    @Mock
    MongoTemplate mongoTemplate;

    @InjectMocks
    OrderService orderService;

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    @Nested
    class Save {
        @Test
        void shouldCallRepositorySave() {
            OrderCreatedEventDTO event = OrderCreatedEventDTOFactory.buildWithOneItem();

            orderService.save(event);

            verify(orderRepository, times(1)).save(any());
        }

        @Test
        void shouldMapEventToEntityWithSuccess() {
            OrderCreatedEventDTO event = OrderCreatedEventDTOFactory.buildWithOneItem();

            orderService.save(event);

            verify(orderRepository, times(1)).save(orderCaptor.capture());
            Order entity = orderCaptor.getValue();

            assertEquals(event.orderId(), entity.getOrderId());
            assertEquals(event.clientId(), entity.getCustomerId());
            assertNotNull(entity.getTotal());
            assertEquals(event.items().get(0).product(), entity.getItems().get(0).getProduct());
            assertEquals(event.items().get(0).quantity(), entity.getItems().get(0).getQuantity());
            assertEquals(event.items().get(0).price(), entity.getItems().get(0).getPrice());
        }

        @Test
        void shouldCalculateOrderTotalWithSuccess() {
            OrderCreatedEventDTO event = OrderCreatedEventDTOFactory.buildWithTwoItems();
            BigDecimal totalItem1 = event.items().get(0).price().multiply(BigDecimal.valueOf(event.items().get(0).quantity()));
            BigDecimal totalItem2 = event.items().get(1).price().multiply(BigDecimal.valueOf(event.items().get(1).quantity()));
            BigDecimal orderTotal = totalItem1.add(totalItem2);

            orderService.save(event);

            verify(orderRepository, times(1)).save(orderCaptor.capture());
            Order entity = orderCaptor.getValue();

            assertNotNull(entity.getTotal());
            assertEquals(orderTotal, entity.getTotal());
        }
    }

    @Nested
    class FindAllByCostumerId {
        @Test
        void shouldCallRepository() {
            Long customerId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10);
            doReturn(OrderFactory.buildWithPage())
                    .when(orderRepository).findAllByCustomerId(eq(customerId), eq(pageRequest));

            orderService.findAllByCostumerId(customerId, pageRequest);

            verify(orderRepository, times(1))
                    .findAllByCustomerId(eq(customerId), eq(pageRequest));
        }

        @Test
        void shouldMapResponse() {
            Long customerId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Order> page = OrderFactory.buildWithPage();
            doReturn(page)
                    .when(orderRepository).findAllByCustomerId(anyLong(), any());

            Page<OrderResponseDTO> response = orderService.findAllByCostumerId(customerId, pageRequest);

            assertEquals(page.getTotalPages(), response.getTotalPages());
            assertEquals(page.getTotalElements(), response.getTotalElements());
            assertEquals(page.getSize(), response.getSize());
            assertEquals(page.getNumber(), response.getNumber());

            assertEquals(page.getContent().get(0).getOrderId(), response.getContent().get(0).orderId());
            assertEquals(page.getContent().get(0).getCustomerId(), response.getContent().get(0).clientId());
            assertEquals(page.getContent().get(0).getTotal(), response.getContent().get(0).total());
        }
    }
}