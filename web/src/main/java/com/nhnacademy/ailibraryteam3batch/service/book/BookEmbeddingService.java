package com.nhnacademy.ailibraryteam3batch.service.book;

import com.nhnacademy.ailibraryteam3batch.dto.embedding.BookEmbeddingTarget;

public interface BookEmbeddingService {

    int generateEmbeddings();

    String createCombinedText(BookEmbeddingTarget book);
}
