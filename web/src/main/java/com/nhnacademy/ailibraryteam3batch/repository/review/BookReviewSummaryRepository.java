package com.nhnacademy.ailibraryteam3batch.repository.review;

import com.nhnacademy.ailibraryteam3batch.domain.review.BookReviewSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewSummaryRepository extends JpaRepository<BookReviewSummary, Long> {
}
