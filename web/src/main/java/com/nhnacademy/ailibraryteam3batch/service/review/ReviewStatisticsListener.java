package com.nhnacademy.ailibraryteam3batch.service.review;

import com.nhnacademy.ailibraryteam3batch.event.ReviewCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewStatisticsListener {

    private final ReviewService reviewService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateReviewSummary(ReviewCreatedEvent event) {
        reviewService.updateReviewSummary(event.bookId());
    }
}
