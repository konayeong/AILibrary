package com.nhnacademy.ailibraryteam3batch.service.book.strategy;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import com.nhnacademy.ailibraryteam3batch.repository.book.BookRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IsbnSearchStrategy implements BookSearchStrategy{

    private final BookRepository bookRepository;

    public IsbnSearchStrategy(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public SearchType getSearchType() {
        return SearchType.ISBN;
    }

    @Override
    @Cacheable(
            value = "team3:isbn",
            key = "#request.searchType() + ':' + #request.isbn()"
    )
    public List<BookSearchResponse> search(BookSearchRequest request) {
        return bookRepository.searchByIsbn(request.isbn());
    }
}
