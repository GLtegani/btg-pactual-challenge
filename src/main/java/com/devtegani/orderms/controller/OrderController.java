package com.devtegani.orderms.controller;

import com.devtegani.orderms.controller.dto.ApiResponseDTO;
import com.devtegani.orderms.controller.dto.OrderResponseDTO;
import com.devtegani.orderms.controller.dto.PaginationResponseDTO;
import com.devtegani.orderms.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    public OrderService orderService;

    @GetMapping("/customers/{customerId}/orders")
    public final ResponseEntity<ApiResponseDTO<OrderResponseDTO>> listOrders(
            @PathVariable("customerId") Long customerId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "18") Integer pageSize

    ) {
        Page<OrderResponseDTO> pageResponse = this
                .orderService
                .findAllByCostumerId(customerId, PageRequest.of(page, pageSize));

        BigDecimal totalOnOrders = this.orderService.findTotalOnOrdersByCustomerId(customerId);

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        Map.of("totalOnOrders", totalOnOrders),
                        pageResponse.getContent(),
                        PaginationResponseDTO.fromPage(pageResponse)
                )
        );
    }
}
