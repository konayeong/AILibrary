package com.nhnacademy.ailibraryteam3batch.repository.book.impl;

import com.nhnacademy.ailibraryteam3batch.domain.book.QBook;
import com.nhnacademy.ailibraryteam3batch.dto.PageCacheDto;
import com.nhnacademy.ailibraryteam3batch.dto.embedding.BookEmbeddingTarget;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.QBookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.repository.book.CustomizedBookRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomizedBookRepositoryImpl implements CustomizedBookRepository {
    private final JPAQueryFactory queryFactory;
    private final QBook book = QBook.book;

    @Override
    public PageCacheDto searchAll(long offset, int pageSize) {
        List<BookSearchResponse> result = queryFactory
                .select(bookProjection())
                .from(book)
                .offset(offset)
                .limit(pageSize)
                .fetch();

        long totalElements = queryFactory
                .select(book.count())
                .from(book)
                .fetchOne();

        return new PageCacheDto(result, totalElements);
    }

    @Override
    public List<BookSearchResponse> searchByKeyword(String keyword) {
        return search(keywordContains(keyword));
    }

    @Override
    public List<BookSearchResponse> searchByIsbn(String isbn) {
        return search(isbnEq(isbn));
    }

    @Override
    public List<BookSearchResponse> vectorSearch(BookSearchRequest request) {
        String vectorString = Arrays.toString(request.vector());
        NumberTemplate<Double> similarityTemplate = Expressions.numberTemplate(
                Double.class,
                "function('vector_cosine_similarity', {0}, {1})",
                book.embedding,
                vectorString
        );


        return queryFactory
                .select(Projections.constructor(
                        BookSearchResponse.class,
                        book.id,
                        book.isbn13,
                        book.title,
                        book.author,
                        book.publisherName,
                        book.price,
                        book.imageUrl,
                        book.content,
                        book.volumeName,
                        similarityTemplate.as("similarity"),
                        Expressions.nullExpression(Double.class),
                        Expressions.nullExpression(Integer.class)
                ))
                .from(book)
                .where(book.embedding.isNotNull(),
                        similarityTemplate.goe(0.5))  // 임베딩이 있는 도서만
                .orderBy(similarityTemplate.desc(),
                        book.id.asc())  // 유사도 높은 순 정렬
                .fetch();
    }

    @Override
    public List<BookEmbeddingTarget> findTop32ByEmbeddingIsNull() {
        return queryFactory.select(Projections.constructor(
                        BookEmbeddingTarget.class,
                        book.id,
                        book.title,
                        book.author,
                        book.content
                ))
                .from(book)
                .where(book.embedding.isNull())
                .limit(32)
                .fetch();
    }

    private List<BookSearchResponse> search(Predicate condition) {
        log.info("검색 Querydsl 작성");
        return
                queryFactory.select(bookProjection())
                        .from(book)
                        .where(condition)
                        .fetch();
    }

    private QBookSearchResponse bookProjection() {
        log.info("BookProjection");
        return new QBookSearchResponse(
                book.id,
                book.isbn13,
                book.title,
                book.author,
                book.publisherName,
                book.price,
                book.imageUrl,
                book.content,
                book.volumeName,
                Expressions.nullExpression(Double.class),
                Expressions.nullExpression(Double.class),
                Expressions.nullExpression(Integer.class)
        );
    }

    // Predicate: 고정된 검색 조건 표현
    // BooleanBuilder: 사용자 입력에 따라 조건을 동적으로 조합할 때 사용
    private Predicate keywordContains(String keyword) {
        if (!hasText(keyword)) {
            return null; // 전체검색
        }
        // 제목, 저자, 출판사명, 본문에서 keyword 검색
        return book.title.containsIgnoreCase(keyword)
                .or(book.author.containsIgnoreCase(keyword))
                .or(book.publisherName.containsIgnoreCase(keyword))
                .or(
                        // QueryDSL -> Hibernate -> SQL
                        Expressions.booleanTemplate(
                                "function('ts_match_korean', {0}, {1}) = true",
                                book.content, keyword
                        )
                );
    }

    private Predicate isbnEq(String isbn) {
        if (!hasText(isbn)) {
            return null;
        }

        return book.isbn13.eq(isbn);
    }
}
