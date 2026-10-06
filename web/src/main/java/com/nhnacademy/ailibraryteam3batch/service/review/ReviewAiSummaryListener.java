package com.nhnacademy.ailibraryteam3batch.service.review;

import com.nhnacademy.ailibraryteam3batch.dto.review.ReviewSummaryTask;
import com.nhnacademy.ailibraryteam3batch.event.ReviewAiSummaryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewAiSummaryListener {

    private final ReviewSummaryQueueService queueService;
    private final ReviewAiSummaryService reviewAiSummaryService;
    private final RedissonClient redissonClient;

    @Async
    @EventListener
    public void handleReviewAiSummaryEvent(ReviewAiSummaryEvent event) {
        queueService.enqueue(event.bookId());
    }

    @RabbitListener(
            queues = "${rabbitmq.queue.review-summary}"
    )
    public void processReviewSummaryTask(ReviewSummaryTask task) {
        Long bookId = task.bookId();
        log.info("RabbitMQ 메시지 수신: {}", bookId);

        String lockKey = "lock:summary:" + bookId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(0, 3, TimeUnit.MINUTES);
            if(!isLocked) {
                log.info("도서 [{}] 이미 요약 수행중", bookId);
                return;
            }

            log.info("도서[{}] AI 요약 작업 시작", bookId);
            reviewAiSummaryService.generateSummary(bookId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("도서[{}] 분산 락 처리 중 인터럽트 발생", bookId, e);
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("도서 [{}] 분산 락 해제 완료", bookId);
            }
        }
    }
}
