package com.nhnacademy.ailibraryteam3batch.service.review;

import com.nhnacademy.ailibraryteam3batch.dto.review.ReviewSummaryTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewSummaryQueueService {

    private final RabbitTemplate rabbitTemplate;
    private static final long DEDUP_WINDOW_MS = 5000;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing-key}")
    private String routingKey = "review.summary";

    public void enqueue(Long bookId) {
        try {
            ReviewSummaryTask task = new ReviewSummaryTask(bookId);
            // 메시지 발행
            rabbitTemplate.convertAndSend(exchange, routingKey, task);
            log.info("도서 [{}] 리뷰 요약 큐 발행", bookId);
        } catch (Exception e) {
            log.error("도서 [{}] 리뷰 요약 큐 발행 실패", bookId, e);
        }
    }
}
