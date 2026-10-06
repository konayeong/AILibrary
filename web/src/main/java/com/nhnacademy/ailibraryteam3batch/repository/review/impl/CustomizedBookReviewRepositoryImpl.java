package com.nhnacademy.ailibraryteam3batch.repository.review.impl;

import com.nhnacademy.ailibraryteam3batch.domain.review.QBookReview;
import com.nhnacademy.ailibraryteam3batch.dto.review.QReviewStatsDto;
import com.nhnacademy.ailibraryteam3batch.dto.review.ReviewStatsDto;
import com.nhnacademy.ailibraryteam3batch.repository.review.CustomizedBookReviewRepository;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomizedBookReviewRepositoryImpl implements CustomizedBookReviewRepository {
    private final JPAQueryFactory queryFactory;
    private final QBookReview review = QBookReview.bookReview;


    @Override
    public ReviewStatsDto selectStat(Long bookId) {
        return queryFactory
                .select(new QReviewStatsDto(
                        review.count(),
                        review.rating.avg(),
                        new CaseBuilder().when(review.rating.eq(1))
                                .then(1).otherwise((Integer) null)
                                .count().intValue(),
                        new CaseBuilder().when(review.rating.eq(2))
                                .then(1).otherwise((Integer) null)
                                .count().intValue(),
                        new CaseBuilder().when(review.rating.eq(3))
                                .then(1).otherwise((Integer) null)
                                .count().intValue(),
                        new CaseBuilder().when(review.rating.eq(4))
                                .then(1).otherwise((Integer) null)
                                .count().intValue(),
                        new CaseBuilder().when(review.rating.eq(5))
                                .then(1).otherwise((Integer) null)
                                .count().intValue()
                ))
                .from(review)
                .where(review.book.id.eq(bookId))
                .fetchOne();
    }
}
