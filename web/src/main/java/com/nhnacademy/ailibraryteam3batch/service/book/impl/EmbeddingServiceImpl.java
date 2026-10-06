package com.nhnacademy.ailibraryteam3batch.service.book.impl;

import com.nhnacademy.ailibraryteam3batch.service.book.EmbeddingService;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingServiceImpl implements EmbeddingService{
    private final EmbeddingModel embeddingModel;

    public EmbeddingServiceImpl(
            @Qualifier("openAiEmbeddingModel")
            EmbeddingModel embeddingModel
    ) {
        this.embeddingModel = embeddingModel;
    }



    @Override
    public float[] getEmbedding(String text) {
        EmbeddingResponse response = embeddingModel.embedForResponse(List.of(text));
        return response.getResults().getFirst().getOutput();
    }

    @Override
    public List<float[]> getEmbeddings(List<String> texts) {
        EmbeddingResponse response = embeddingModel.embedForResponse(texts);
        return response.getResults().stream()
                .map(Embedding::getOutput)
                .toList();
    }
}
