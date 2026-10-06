package com.nhnacademy.ailibraryteam3batch.dto.search;

import jakarta.validation.constraints.Size;

// 도서 검색 요청
public record BookSearchRequest (

        @Size(max = 100, message = "검색어는 100자 이하여야 합니다.")
        String keyword,

        @Size(max = 13, message = "ISBN은 13자 이하여야 합니다.")
        String isbn,

        SearchType searchType,

        float[] vector
) {
}
