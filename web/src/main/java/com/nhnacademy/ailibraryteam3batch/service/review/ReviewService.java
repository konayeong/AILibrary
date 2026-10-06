package com.nhnacademy.ailibraryteam3batch.service.review;

import com.nhnacademy.ailibraryteam3batch.domain.book.Book;
import com.nhnacademy.ailibraryteam3batch.domain.review.BookReview;
import com.nhnacademy.ailibraryteam3batch.domain.review.BookReviewSummary;
import com.nhnacademy.ailibraryteam3batch.dto.review.BookReviewDto;
import com.nhnacademy.ailibraryteam3batch.dto.review.ReviewCreateRequest;
import com.nhnacademy.ailibraryteam3batch.dto.review.ReviewStatsDto;
import com.nhnacademy.ailibraryteam3batch.event.ReviewAiSummaryEvent;
import com.nhnacademy.ailibraryteam3batch.event.ReviewCreatedEvent;
import com.nhnacademy.ailibraryteam3batch.exception.BookNotFoundException;
import com.nhnacademy.ailibraryteam3batch.repository.book.BookRepository;
import com.nhnacademy.ailibraryteam3batch.repository.review.BookReviewRepository;
import com.nhnacademy.ailibraryteam3batch.repository.review.BookReviewSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final BookRepository bookRepository;
    private final BookReviewRepository reviewRepository;
    private final BookReviewSummaryRepository reviewSummaryRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createReview(Long bookId, ReviewCreateRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        BookReview review = new BookReview(book, request.content(), request.rating());
        reviewRepository.save(review);

        eventPublisher.publishEvent(new ReviewCreatedEvent(book.getId()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateReviewSummary(Long bookId) {
        ReviewStatsDto stats = reviewRepository.selectStat(bookId);

        BookReviewSummary summary = reviewSummaryRepository.findById(bookId)
                .orElseGet(() -> {
                    Book referenceBook = bookRepository.getReferenceById(bookId);
                    return new BookReviewSummary(referenceBook);
                });

        summary.update(stats);
        reviewSummaryRepository.save(summary);

        eventPublisher.publishEvent(new ReviewAiSummaryEvent(bookId));
    }

    @Transactional(readOnly = true)
    public Page<BookReviewDto> getReviewList(Long bookId, Pageable pageable) {
        return reviewRepository.findAllByBookIdOrderByIdDesc(bookId, pageable)
                .map(BookReviewDto::from);
    }

    @Transactional(readOnly = true)
    public ReviewStatsDto getReviewSummary(Long bookId) {
        return reviewRepository.selectStat(bookId);
    }

    @Transactional(readOnly = true)
    public String getAiSummaryMessage(Long bookId) {
        Optional<BookReviewSummary> optionalSummary = reviewSummaryRepository.findById(bookId);

        if(optionalSummary.isPresent()) {
            BookReviewSummary summary = optionalSummary.get();

            if(summary.getReviewCount() < 10) {
                return "리뷰가 모이고 있습니다. 곧 AI 요약이 제공될 예정입니다.";
            }

            if (summary.getReviewSummary() == null || summary.getReviewSummary().isBlank()) {
                return "AI가 리뷰를 분석 중입니다. 잠시 후 다시 확인해주세요.";
            }

            return summary.getReviewSummary();
        }
        return "아직 등록된 리뷰가 없습니다. 첫 번째 리뷰를 남겨보세요!";
    }
}
