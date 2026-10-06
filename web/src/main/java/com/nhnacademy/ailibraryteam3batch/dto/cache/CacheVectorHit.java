package com.nhnacademy.ailibraryteam3batch.dto.cache;

public record CacheVectorHit(
        SemanticCache cache,
        double score
) {}