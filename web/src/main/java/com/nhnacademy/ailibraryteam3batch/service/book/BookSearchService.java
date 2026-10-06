package com.nhnacademy.ailibraryteam3batch.service.book;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookSearchService {
    Page<BookSearchResponse> search(Pageable pageable, BookSearchRequest request);
}
