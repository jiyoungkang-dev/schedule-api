package com.example.schedule_api.dto;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * Page 를 그대로 직렬화하지 않고 필요한 페이징 정보만 노출하는 공용 응답 DTO.
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
