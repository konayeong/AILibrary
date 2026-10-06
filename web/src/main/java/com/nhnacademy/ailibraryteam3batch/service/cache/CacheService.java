package com.nhnacademy.ailibraryteam3batch.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CacheService {

    List<BookSearchResponse> get(SearchType type, String keyword, String context);

    void put(SearchType type, String keyword, String context, List<BookSearchResponse> fullResult) throws JsonProcessingException;

    void delete(SearchType type, String key);

}
