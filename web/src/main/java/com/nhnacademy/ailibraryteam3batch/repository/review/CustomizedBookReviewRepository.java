package com.nhnacademy.ailibraryteam3batch.repository.review;

import com.nhnacademy.ailibraryteam3batch.dto.review.ReviewStatsDto;

public interface CustomizedBookReviewRepository {
    ReviewStatsDto selectStat(Long bookId);
}
