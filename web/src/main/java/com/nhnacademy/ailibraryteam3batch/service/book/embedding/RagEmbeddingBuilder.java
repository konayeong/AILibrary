package com.nhnacademy.ailibraryteam3batch.service.book.embedding;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import org.springframework.stereotype.Component;

@Component
public class RagEmbeddingBuilder implements EmbeddingBuilder{
    @Override
    public SearchType getSearchType() {
        return SearchType.RAG;
    }

    @Override
    public String build() {
        return "사용자의 질문에 답변하는 데 도움이 되는 관련 도서를 의미 기반으로 검색한다. 검색어: ";
    }
}
