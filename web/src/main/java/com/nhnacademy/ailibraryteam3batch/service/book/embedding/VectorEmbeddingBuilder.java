package com.nhnacademy.ailibraryteam3batch.service.book.embedding;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import org.springframework.stereotype.Component;

@Component
public class VectorEmbeddingBuilder implements EmbeddingBuilder {

    @Override
    public SearchType getSearchType() {
        return SearchType.VECTOR;
    }

    @Override
    public String build() {
        return "입력된 질문의 의미와 개념을 기준으로 가장 유사한 프로그래밍 및 소프트웨어 관련 도서를 검색한다. 검색어: ";
    }
}