package com.nhnacademy.ailibraryteam3batch.service.cache;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RedisService {
    List<BookSearchResponse> redisSearch(SearchType type, String keyword, String context);

    void putCache(SearchType type, String keyword, String context, List<BookSearchResponse> fullResult);
}
