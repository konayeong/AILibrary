package com.nhnacademy.ailibraryteam3batch.scheduler;


import com.nhnacademy.ailibraryteam3batch.repository.cache.BookSearchCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 캐시 정리 스케줄러
 *
 * 주기적으로 만료된 캐시를 삭제합니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CacheCleanupScheduler {

    private final BookSearchCacheRepository cacheRepository;

    /**
     * 매시간 정각에 만료된 캐시 정리
     */
    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredCaches() {
        log.info("만료된 캐시 정리 시작");

        int deletedCount = cacheRepository.deleteExpiredCaches();

        log.info("만료된 캐시 정리 완료: {}개 삭제", deletedCount);
    }
}
