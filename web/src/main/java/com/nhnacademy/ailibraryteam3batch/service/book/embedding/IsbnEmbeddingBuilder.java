package com.nhnacademy.ailibraryteam3batch.service.book.embedding;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import org.springframework.stereotype.Component;

@Component
public class IsbnEmbeddingBuilder implements EmbeddingBuilder{
    @Override
    public SearchType getSearchType() {
        return SearchType.ISBN;
    }

    @Override
    public String build() {
        return "도서의 고유 식별자인 ISBN 번호를 기반으로 정확히 일치하는 도서를 검색한다. 검색어: ";
    }
}
