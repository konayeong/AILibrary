package com.nhnacademy.ailibraryteam3batch.service.book.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.ailibraryteam3batch.dto.PageCacheDto;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import com.nhnacademy.ailibraryteam3batch.repository.book.BookRepository;
import com.nhnacademy.ailibraryteam3batch.service.book.BookSearchService;
import com.nhnacademy.ailibraryteam3batch.service.cache.RedisService;
import com.nhnacademy.ailibraryteam3batch.service.book.embedding.EmbeddingBuilderFactory;
import com.nhnacademy.ailibraryteam3batch.service.book.strategy.BookSearchStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {
    private final BookRepository bookRepository;
    private final RedisService redisService;
    private final BookSearchStrategyFactory bookFactory;
    private final EmbeddingBuilderFactory embeddingBuilderFactory;

    @Override
    public Page<BookSearchResponse> search(Pageable pageable, BookSearchRequest request) {
        SearchType type = request.searchType();
        String keyword = request.keyword();

        List<BookSearchResponse> result = new ArrayList<>();

        //어노테이션캐싱
        if (SearchType.KEYWORD == type || SearchType.ISBN == type) {
            return toPage(pageable, bookFactory.get(type).search(request));
        }

        if (type == null || keyword.isBlank()) {
            PageCacheDto pageCacheDto = bookRepository.searchAll(pageable.getOffset(), pageable.getPageSize());

            return new PageImpl<>(pageCacheDto.content(), pageable, pageCacheDto.totalElements());
        }

        String context = embeddingBuilderFactory.get(type).build();
        result = redisService.redisSearch(type, keyword, context);

        if (result != null && !result.isEmpty()) {
            return toPage(pageable, result);
        }

        result = bookFactory.get(type).search(request);
        redisService.putCache(type, keyword, context, result);

        return toPage(pageable, result);
    }

    public <T> Page<T> toPage(Pageable pageable, List<T> list) {

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());

        List<T> content =
                (start >= list.size()) ? List.of() : list.subList(start, end);

        return new PageImpl<>(content, pageable, list.size());
    }
}
