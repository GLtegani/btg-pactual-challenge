package com.devtegani.orderms.controller;

import com.devtegani.orderms.controller.dto.ApiResponseDTO;
import com.devtegani.orderms.controller.dto.OrderResponseDTO;
import com.devtegani.orderms.factory.OrderResponseFactory;
import com.devtegani.orderms.services.OrderService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController orderController;

    @Captor
    ArgumentCaptor<Long> customerIdCaptor;

    @Captor
    ArgumentCaptor<PageRequest> pageRequestCaptor;

    @Nested
    class ListOrders {
        @Test
        void shouldReturnHttpOk() {
            Long customerId = 1L;
            Integer page = 0;
            Integer pageSize = 10;
            doReturn(OrderResponseFactory.buildWithOneItem())
                    .when(orderService)
                    .findAllByCostumerId(anyLong(), any());

            doReturn(BigDecimal.valueOf(20.50))
                    .when(orderService)
                    .findTotalOnOrdersByCustomerId(anyLong());

            ResponseEntity<ApiResponseDTO<OrderResponseDTO>> response = orderController
                    .listOrders(customerId, page, pageSize);

            assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
        }

        @Test
        void shouldPassCorrectParametersToService() {
            Long customerId = 1L;
            Integer page = 0;
            Integer pageSize = 10;
            doReturn(OrderResponseFactory.buildWithOneItem())
                    .when(orderService)
                    .findAllByCostumerId(customerIdCaptor.capture(), pageRequestCaptor.capture());

            doReturn(BigDecimal.valueOf(20.50))
                    .when(orderService)
                    .findTotalOnOrdersByCustomerId(customerIdCaptor.capture());

            ResponseEntity<ApiResponseDTO<OrderResponseDTO>> response = orderController
                    .listOrders(customerId, page, pageSize);

            assertEquals(2, customerIdCaptor.getAllValues().size());
            assertEquals(customerId, customerIdCaptor.getAllValues().get(0));
            assertEquals(customerId, customerIdCaptor.getAllValues().get(1));
            assertEquals(page, pageRequestCaptor.getValue().getPageNumber());
            assertEquals(pageSize, pageRequestCaptor.getValue().getPageSize());
        }

        @Test
        void shouldReturnResponseBodyCorrectly() {
            Long customerId = 1L;
            Integer page = 0;
            Integer pageSize = 10;
            BigDecimal totalOnOrders = BigDecimal.valueOf(20.50);
            Page<OrderResponseDTO> pagination = OrderResponseFactory.buildWithOneItem();

            doReturn(pagination)
                    .when(orderService)
                    .findAllByCostumerId(anyLong(), any());

            doReturn(totalOnOrders)
                    .when(orderService)
                    .findTotalOnOrdersByCustomerId(anyLong());

            ResponseEntity<ApiResponseDTO<OrderResponseDTO>> response = orderController
                    .listOrders(customerId, page, pageSize);

            assertNotNull(response);
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().data());
            assertNotNull(response.getBody().pagination());
            assertNotNull(response.getBody().summary());
            assertEquals(totalOnOrders, response.getBody().summary().get("totalOnOrders"));
            assertEquals(pagination.getTotalElements(), response.getBody().pagination().totalElements());
            assertEquals(pagination.getTotalPages(), response.getBody().pagination().totalPages());
            assertEquals(pagination.getNumber(), response.getBody().pagination().page());
            assertEquals(pagination.getSize(), response.getBody().pagination().pageSize());
            assertEquals(pagination.getContent(), response.getBody().data());
        }
    }
}