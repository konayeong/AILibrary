package com.nhnacademy.ailibraryteam3batch.dto.cache;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import lombok.Getter;

import java.util.List;

public record SemanticCache (
        String question,
        SearchType searchType,
        float[] embedding,
        List<BookSearchResponse> books,
        Long totalBookCount
) {

}