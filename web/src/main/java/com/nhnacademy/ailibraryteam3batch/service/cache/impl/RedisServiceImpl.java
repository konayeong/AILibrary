package com.nhnacademy.ailibraryteam3batch.service.cache.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ailibraryteam3batch.dto.cache.SemanticCache;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import com.nhnacademy.ailibraryteam3batch.service.book.strategy.KeywordSearchStrategy;
import com.nhnacademy.ailibraryteam3batch.service.cache.CacheService;
import com.nhnacademy.ailibraryteam3batch.service.cache.RedisService;
import com.nhnacademy.ailibraryteam3batch.service.book.EmbeddingService;
import com.nhnacademy.ailibraryteam3batch.service.cache.SemanticSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CacheService cacheService;
    private final SemanticSearchService semanticSearchService;
    private final KeywordSearchStrategy keywordSearchStrategy;

    @Override
    public List<BookSearchResponse> redisSearch(SearchType type, String keyword, String context) {

        // vector
        return semanticSearchService.cacheVectorSearch(context + keyword);
    }

    @Override
    public void putCache(SearchType type, String keyword, String context, List<BookSearchResponse> fullResult) {
        try {
            cacheService.put(type, keyword, context, fullResult);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
