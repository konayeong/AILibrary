package com.nhnacademy.ailibraryteam3batch.domain.book;

import com.nhnacademy.ailibraryteam3batch.domain.review.BookReviewSummary;
import com.nhnacademy.ailibraryteam3batch.dto.book.BookRawData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "book_sequence_generator")
    @SequenceGenerator(name = "book_sequence_generator",
            sequenceName = "public.book_sequence",
            allocationSize = 1000)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 13, unique = true)
    private String isbn13;

    @Column(length = 25)
    private String volumeName;

    @NotNull
    @Column(nullable = false, length = 1000)
    private String title;

    @Column(length = 1000)
    private String author;

    @Column(length = 255)
    private String publisherName;

    private LocalDate firstPublishedDate;

    @Column(length = 5)
    private String additionSymbol;

    private Integer price;

    @Column(length = 255)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 25)
    private String kdcCode;

    @Column(length = 1000)
    private String subTitle;

    @Column(length = 1000)
    private String subAuthor;

    private LocalDate secondPublishedDate;

    private Boolean isBookstorePresent;

    private Boolean isPortalPresent;

    @Column(name = "embedding", columnDefinition = "vector(1024)")
    @JdbcTypeCode(SqlTypes.VECTOR)
    private float[] embedding;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookReviewSummary reviewSummary;

    private Book(String isbn13, String volumeName, String title, String author, String publisherName, LocalDate firstPublishedDate, String additionSymbol, Integer price,
                 String imageUrl, String content, String kdcCode, String subTitle, String subAuthor, LocalDate secondPublishedDate, Boolean isBookstorePresent, Boolean isPortalPresent
    ) {
        this.isbn13 = isbn13;
        this.volumeName = volumeName;
        this.title = title;
        this.author = author;
        this.publisherName = publisherName;
        this.firstPublishedDate = firstPublishedDate;
        this.additionSymbol = additionSymbol;
        this.price = price;
        this.imageUrl = imageUrl;
        this.content = content;
        this.kdcCode = kdcCode;
        this.subTitle = subTitle;
        this.subAuthor = subAuthor;
        this.secondPublishedDate = secondPublishedDate;
        this.isBookstorePresent = isBookstorePresent;
        this.isPortalPresent = isPortalPresent;
    }

    public static Book from(BookRawData rawData) {
        return new Book(
                rawData.isbn(),
                rawData.volumeName(),
                rawData.title(),
                rawData.author(),
                rawData.publisherName(),
                rawData.firstPublishDate(),
                rawData.additionSymbol(),
                rawData.price(),
                rawData.image(),
                rawData.content(),
                rawData.kdc(),
                rawData.subTitle(),
                rawData.subAuthor(),
                rawData.secondPublishDate(),
                rawData.isBookStorePresent(),
                rawData.isPortalPresent()
        );
    }
}
