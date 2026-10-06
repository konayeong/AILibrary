package com.nhnacademy.ailibraryteam3batch.service.book.strategy;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookSearchStrategy {

    SearchType getSearchType();

    List<BookSearchResponse> search(BookSearchRequest request);
}
