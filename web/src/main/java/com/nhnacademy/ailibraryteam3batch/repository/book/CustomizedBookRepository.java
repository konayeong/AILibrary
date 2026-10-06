package com.nhnacademy.ailibraryteam3batch.repository.book;

import com.nhnacademy.ailibraryteam3batch.dto.PageCacheDto;
import com.nhnacademy.ailibraryteam3batch.dto.embedding.BookEmbeddingTarget;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * QueryDSL을 사용한 복잡한 검색 기능 정의
 */
public interface CustomizedBookRepository {
    PageCacheDto searchAll(long offset, int pageSize);
    List<BookSearchResponse> searchByKeyword(String keyword);
    List<BookSearchResponse> searchByIsbn(String isbn);
    List<BookSearchResponse> vectorSearch(BookSearchRequest request);
    List<BookEmbeddingTarget> findTop32ByEmbeddingIsNull();
}
