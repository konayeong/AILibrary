package com.nhnacademy.ailibraryteam3batch.service.cache;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;

import java.util.List;

public interface SemanticSearchService {

    List<BookSearchResponse> cacheVectorSearch(String context);
}
