package com.nhnacademy.ailibraryteam3batch.domain.review;

import com.nhnacademy.ailibraryteam3batch.domain.book.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "book_review")
@Getter
@NoArgsConstructor
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    public BookReview(Book book, String content, Integer rating) {
        this.book = book;
        this.content = content;
        this.rating = rating;
        this.createdAt = OffsetDateTime.now();
    }
}
