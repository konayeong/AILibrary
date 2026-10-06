package com.nhnacademy.ailibraryteam3batch.dto.search;

import org.springframework.data.domain.Page;
import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    public int startPage() {
        return (page / 10) * 10;
    }

    public int endPage() {
        return Math.min(startPage() + 9, totalPages - 1);
    }
}
