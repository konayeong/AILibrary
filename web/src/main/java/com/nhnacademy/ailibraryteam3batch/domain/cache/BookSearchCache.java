package com.nhnacademy.ailibraryteam3batch.domain.cache;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;


/**
 * 도서 검색 캐시 엔티티
 * 결과 저장 후 재사용
 */

@Entity
@Table(name = "book_search_cache")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookSearchCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 검색질문
     */
    @Column(nullable = false, length = 500)
    private String query;

    /**
     * 질문 임베딩 (시멘틱 서칭)
     */
    @Column(nullable = false, columnDefinition = "vector(1024)")
    @JdbcTypeCode(SqlTypes.VECTOR)
    private float[] queryEmbedding;

    /**
     * 캐시된 검색 결과(JSON)
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String result;

    /**
     * 생성 시간
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * 마지막 접근 시간
     */
    @Column(nullable = false)
    private LocalDateTime lastAccessedAt;

    /**
     * 접근 횟수
     */
    @Column(nullable = false)
    private Integer accessCount;

    /**
     * 초 단위
     */
    @Column(nullable = false)
    private Integer ttl;
}
