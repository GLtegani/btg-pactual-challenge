package com.devtegani.orderms.controller.dto;

import org.springframework.data.domain.Page;

public record PaginationResponseDTO(
        Integer page,
        Integer pageSize,
        Long totalElements,
        Integer totalPages
) {
    public static PaginationResponseDTO fromPage(Page<?> page) {
        return new PaginationResponseDTO(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
