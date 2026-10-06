package com.nhnacademy.ailibraryteam3batch.service.review;

import com.nhnacademy.ailibraryteam3batch.domain.book.Book;
import com.nhnacademy.ailibraryteam3batch.domain.review.BookReview;
import com.nhnacademy.ailibraryteam3batch.domain.review.BookReviewSummary;
import com.nhnacademy.ailibraryteam3batch.repository.book.BookRepository;
import com.nhnacademy.ailibraryteam3batch.repository.review.BookReviewRepository;
import com.nhnacademy.ailibraryteam3batch.repository.review.BookReviewSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewAiSummaryService {

    private final BookRepository bookRepository;
    private final BookReviewRepository reviewRepository;
    private final BookReviewSummaryRepository summaryRepository;
    private final ReviewSummarizer reviewSummarizer;


    @Transactional
    public void generateSummary(Long bookID) {
        BookReviewSummary summary = summaryRepository.findById(bookID)
                .orElseGet(() -> {
                    Book referenceBook = bookRepository.getReferenceById(bookID);
                    return new BookReviewSummary(referenceBook);
                });

        if(!summary.shouldGenerateSummary()) {
            log.info("도서 [{}] 요약 조건 미달", bookID);
            return;
        }

        Long bookId = summary.getId();
        String finalSummary;
        List<BookReview> targetReviews;

        String reviewSummary = summary.getReviewSummary();
        long newReviewCount = summary.getReviewCount() - summary.getLastSummarizedCount();

        List<BookReview> allReviews = reviewRepository.findAllByBookIdOrderByIdAsc(bookId);

        // Drift 방지 및 최초 요약 (30개 이상 누적 시 전체 재요약)
        if (reviewSummary == null || newReviewCount >= 30) {
            targetReviews = filterValidReviews(allReviews);

            if (targetReviews.isEmpty()) {
                log.info("도서 [{}] 유효한 리뷰가 없어 요약을 건너뜁니다.", bookId);
                summary.updateSummary(reviewSummary, summary.getReviewCount());
                return;
            }

            log.info("전체 요약 실행: {}건", targetReviews.size());
            finalSummary = reviewSummarizer.summarizeReviews(targetReviews);

        } else {
            List<BookReview> newReviews = allReviews.stream()
                    .skip(summary.getLastSummarizedCount())
                    .toList();

            targetReviews = filterValidReviews(newReviews);

            if (targetReviews.isEmpty()) {
                log.info("도서 [{}] 새로 추가된 리뷰 중 요약에 적합한 텍스트가 없어 DB 갱신만 수행합니다.", bookId);
                summary.updateSummary(reviewSummary, summary.getReviewCount());
                return;
            }

            log.info("누적 요약 실행: 추가 {}건", targetReviews.size());
            finalSummary = reviewSummarizer.summarizeIncremental(reviewSummary, targetReviews);
        }

        summary.updateSummary(finalSummary, summary.getReviewCount());
        summaryRepository.save(summary);
    }

    private List<BookReview> filterValidReviews(List<BookReview> rawReviews) {
        return rawReviews.stream()
                .filter(review -> review.getContent() != null)
                // 공백 미포함 20자 이상인 리뷰만 유효
                .filter(review -> review.getContent().trim().length() >= 20)
                // 의미 없는 자음/모음 반복 거름
                .filter(review -> !review.getContent().matches("^[ㄱ-ㅎㅏ-ㅣ\\s]+$"))
                .toList();
    }
}
