package com.nhnacademy.ailibraryteam3batch.dto.review;

public record ReviewSummaryTask(
        Long bookId,
        long timestamp
) {
    public ReviewSummaryTask(Long bookId) {
        this(bookId, System.currentTimeMillis());
    }
}
