package com.nhnacademy.ailibraryteam3batch.dto.review;

import com.nhnacademy.ailibraryteam3batch.domain.review.BookReview;

import java.time.OffsetDateTime;

public record BookReviewDto(
        Long id,
        String content,
        Integer rating,
        OffsetDateTime createdAt
) {
    public static BookReviewDto from(BookReview review) {
        return new BookReviewDto(review.getId(), review.getContent(), review.getRating(), review.getCreatedAt());
    }
}
