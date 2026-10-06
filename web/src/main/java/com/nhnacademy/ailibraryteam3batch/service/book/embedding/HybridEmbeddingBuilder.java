package com.nhnacademy.ailibraryteam3batch.service.book.embedding;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import org.springframework.stereotype.Component;

@Component
public class HybridEmbeddingBuilder implements EmbeddingBuilder{
    @Override
    public SearchType getSearchType() {
        return SearchType.HYBRID;
    }

    @Override
    public String build() {
        return "키워드와 의미 유사도를 함께 고려하여 사용자의 검색어와 관련된 도서를 균형 있게 검색한다. 검색어: ";
    }
}
