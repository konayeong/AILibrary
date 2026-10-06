package com.nhnacademy.ailibraryteam3batch.dto.review;

import com.querydsl.core.annotations.QueryProjection;

public record ReviewStatsDto(
        Long reviewCount,
        Double averageRating,
        Integer rating1Count,
        Integer rating2Count,
        Integer rating3Count,
        Integer rating4Count,
        Integer rating5Count
) {
    @QueryProjection
    public ReviewStatsDto {}
}
