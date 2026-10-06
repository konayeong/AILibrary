package com.nhnacademy.ailibraryteam3batch.domain.review;

import com.nhnacademy.ailibraryteam3batch.domain.book.Book;
import com.nhnacademy.ailibraryteam3batch.dto.review.ReviewStatsDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_review_summary")
@Getter
@NoArgsConstructor
public class BookReviewSummary {

    @Id
    @Column(name = "book_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(nullable = false)
    private Long reviewCount = 0L;

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer rating1Count = 0;
    @Column(nullable = false)
    private Integer rating2Count = 0;
    @Column(nullable = false)
    private Integer rating3Count = 0;
    @Column(nullable = false)
    private Integer rating4Count = 0;
    @Column(nullable = false)
    private Integer rating5Count = 0;

    @Column
    private LocalDateTime lastReviewedAt;

    @Column(columnDefinition = "TEXT")
    private String reviewSummary;

    @Setter
    @Column(nullable = false)
    private Boolean isSummaryDirty = true;

    @Column(nullable = false)
    private Long lastSummarizedCount = 0L;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public BookReviewSummary(Book book) {
        this.id = book.getId();
        this.book = book;
        this.updatedAt = LocalDateTime.now();
    }

    public BookReviewSummary(Long id, Long reviewCount, BigDecimal averageRating, Integer rating1Count,
                             Integer rating2Count, Integer rating3Count, Integer rating4Count, Integer rating5Count,
                             LocalDateTime lastReviewedAt) {
        this.id = id;
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
        this.rating1Count = rating1Count;
        this.rating2Count = rating2Count;
        this.rating3Count = rating3Count;
        this.rating4Count = rating4Count;
        this.rating5Count = rating5Count;
        this.lastReviewedAt = lastReviewedAt;
    }

    public void update(ReviewStatsDto stats) {
        this.reviewCount = stats.reviewCount();
        this.averageRating = BigDecimal.valueOf(stats.averageRating());

        this.rating1Count = stats.rating1Count();
        this.rating2Count = stats.rating2Count();
        this.rating3Count = stats.rating3Count();
        this.rating4Count = stats.rating4Count();
        this.rating5Count = stats.rating5Count();

        this.isSummaryDirty = true;
    }

    public boolean shouldGenerateSummary() {
        return (reviewCount - lastSummarizedCount) >= 10 && isSummaryDirty;
    }

    public void updateSummary(String reviewSummary, Long currentReviewCount) {
        this.reviewSummary = reviewSummary;
        this.isSummaryDirty = false;
        this.lastSummarizedCount = currentReviewCount;
        this.lastReviewedAt = LocalDateTime.now();
    }
}
