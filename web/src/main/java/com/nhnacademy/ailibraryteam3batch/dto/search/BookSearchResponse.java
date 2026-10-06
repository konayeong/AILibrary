package com.nhnacademy.ailibraryteam3batch.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 도서 검색 응답 class
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // redis에서 가져올 떄 없으면 무시
public class BookSearchResponse {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisherName;
    private Integer price;
    private String imageUrl;
    @Setter
    private String content;
    private String volumeName;
    private Double similarity;
    @Setter
    private Double rrfScore;
    private Integer relevant;


    @QueryProjection // 응답 객체에 Querydsl 의존 발생
    public BookSearchResponse(Long id, String isbn, String title, String author, String publisherName, Integer price, String imageUrl, String content, String volumeName, Double similarity, Double rrfScore, Integer relevant) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisherName = publisherName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.content = content;
        this.volumeName = volumeName;
        this.similarity = similarity;
        this.rrfScore = rrfScore;
        this.relevant = relevant;
    }

    // 유사도 퍼센트 변환
    public String getSimilarityPercent() {
        if (similarity == null) {
            return null;
        }
        return String.format("%.1f%%", similarity * 100);
    }
}
