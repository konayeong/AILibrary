package com.nhnacademy.ailibraryteam3batch.dto;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;

import java.util.List;

public record PageCacheDto(
        List<BookSearchResponse> content,
        long totalElements
) {
}
