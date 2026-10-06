package com.nhnacademy.ailibraryteam3batch.service.cache.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ailibraryteam3batch.dto.cache.SemanticCache;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import com.nhnacademy.ailibraryteam3batch.service.book.EmbeddingService;
import com.nhnacademy.ailibraryteam3batch.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EmbeddingService embeddingService;
    private final JedisPooled jedis;

    @Override
    public List<BookSearchResponse> get(SearchType type, String keyword, String context) {
        String normalized = keyword.trim().toLowerCase();
        String key = String.format("team3:semantic:%s:%s", type, normalized);

        Object cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {

            SemanticCache semanticCache =
                    objectMapper.convertValue(
                            cached,
                            new TypeReference<SemanticCache>() {}
                    );

            return semanticCache.books();
        }

        return List.of();
    }

    @Override
    public void put(SearchType type, String keyword, String context, List<BookSearchResponse> fullResult) throws JsonProcessingException {
        String normalized = keyword.trim().toLowerCase();
        String key = String.format("team3:semantic:%s:%s", type, normalized);

        float[] embeddedContext = embeddingService.getEmbedding(context + keyword);

        SemanticCache semanticCache = new SemanticCache(keyword, type, embeddedContext, fullResult, (long) fullResult.size());

        String json = objectMapper.writeValueAsString(semanticCache);

        jedis.jsonSet(key, json);
        jedis.expire(key, 3600);
    }

    @Override
    public void delete(SearchType type, String keyword) {
        String normalized = keyword.trim().toLowerCase();
        String key = String.format("team3:semantic:%s:%s", type, normalized);
        redisTemplate.delete(key);
    }
}
