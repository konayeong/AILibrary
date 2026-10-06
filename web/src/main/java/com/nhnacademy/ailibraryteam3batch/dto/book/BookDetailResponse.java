package com.nhnacademy.ailibraryteam3batch.dto.book;

import com.nhnacademy.ailibraryteam3batch.domain.book.Book;

import java.time.LocalDate;

/**
 * 도서 상세 페이지 조회
 */
public record BookDetailResponse(
        String title,
        String imageUrl,
        String author,
        String publisherName,
        LocalDate publishedDate,
        String isbn,
        Integer price,
        String subTitle,
        String content
) {
    public static BookDetailResponse from(Book book) {
        return new BookDetailResponse(
                book.getTitle(),
                book.getImageUrl(),
                book.getAuthor(),
                book.getPublisherName(),
                book.getSecondPublishedDate(), // First는 null값만 있음
                book.getIsbn13(),
                book.getPrice() == null ? 0  : book.getPrice(),
                book.getSubTitle(),
                book.getContent()
        );
    }
}
