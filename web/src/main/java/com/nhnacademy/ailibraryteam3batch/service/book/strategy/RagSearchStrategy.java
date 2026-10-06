package com.nhnacademy.ailibraryteam3batch.service.book.strategy;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import com.nhnacademy.ailibraryteam3batch.service.book.BookRagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RagSearchStrategy implements BookSearchStrategy{

    private final HybridSearchStrategy hybridSearchStrategy;
    private final BookRagService bookRagService;

    @Override
    public SearchType getSearchType() {
        return SearchType.RAG;
    }

    @Override
    public List<BookSearchResponse> search(BookSearchRequest request) {
        List<BookSearchResponse> hybridResult = hybridSearchStrategy.search(request);

        return bookRagService.ragSearch(request.keyword(), hybridResult);
    }
}
