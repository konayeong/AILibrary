package com.nhnacademy.ailibraryteam3batch.service.book.strategy;

import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchRequest;
import com.nhnacademy.ailibraryteam3batch.dto.search.BookSearchResponse;
import com.nhnacademy.ailibraryteam3batch.dto.search.SearchType;
import com.nhnacademy.ailibraryteam3batch.repository.book.BookRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VectorSearchStrategy implements BookSearchStrategy{

    private final EmbeddingModel embeddingModel;
    private final BookRepository bookRepository;

    public VectorSearchStrategy(@Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel,
                                BookRepository bookRepository) {
        this.embeddingModel = embeddingModel;
        this.bookRepository = bookRepository;

    }

    @Override
    public SearchType getSearchType() {
        return SearchType.VECTOR;
    }

    @Override
    public List<BookSearchResponse> search(BookSearchRequest request) {
        float[] vector = embeddingModel.embed(request.keyword());

        BookSearchRequest vectorRequest = new BookSearchRequest(
                request.keyword(),
                request.isbn(),
                SearchType.VECTOR,
                vector
        );
        return bookRepository.vectorSearch(vectorRequest);
    }
}
