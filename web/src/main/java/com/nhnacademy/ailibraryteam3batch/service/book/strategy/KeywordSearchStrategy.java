package com.nhnacademy.ailibraryteam3batch.service.book.strategy;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import com.nhnacademy.ailibraryteam3batch.repository.book.BookRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeywordSearchStrategy implements BookSearchStrategy{

    private final BookRepository bookRepository;

    public KeywordSearchStrategy(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public SearchType getSearchType() {
        return SearchType.KEYWORD;
    }

    @Override
    @Cacheable(
            value = "team3:keyword",
            key = "#request.searchType() + ':' + #request.keyword().trim().toLowerCase()"
    )
    public List<BookSearchResponse> search(BookSearchRequest request) {
        return bookRepository.searchByKeyword(request.keyword());
    }
}
