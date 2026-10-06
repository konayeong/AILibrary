package com.nhnacademy.ailibraryteam3batch.repository.review;

import com.nhnacademy.ailibraryteam3batch.domain.review.BookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReview, Long>, CustomizedBookReviewRepository {
    List<BookReview> findAllByBookIdOrderByIdAsc(Long bookId);
    Page<BookReview> findAllByBookIdOrderByIdDesc(Long bookId, Pageable pageable);
}
