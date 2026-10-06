package com.nhnacademy.ailibraryteam3batch.dto.cache;

public enum CacheStatus {
    HIT, // 캐싱된 값이 있고 페이징도 가능
    PARTIAL, // 캐싱이 있으나 페이징 불가
    MISS // 캐싱이 맞는게 없음
}