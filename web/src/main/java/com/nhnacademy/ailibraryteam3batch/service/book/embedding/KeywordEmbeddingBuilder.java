package com.nhnacademy.ailibraryteam3batch.service.book.embedding;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import org.springframework.stereotype.Component;

@Component
public class KeywordEmbeddingBuilder implements EmbeddingBuilder {

    @Override
    public SearchType getSearchType() {
        return SearchType.KEYWORD;
    }

    @Override
    public String build() {
        return "키워드 기반으로 입력된 단어와 일치하거나 유사한 도서를 검색한다. 검색어: ";
    }
}