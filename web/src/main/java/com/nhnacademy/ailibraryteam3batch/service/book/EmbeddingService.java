package com.nhnacademy.ailibraryteam3batch.service.book;

import java.util.List;

public interface EmbeddingService {

    // 단일 텍스트 임베딩
    float[] getEmbedding(String text);

    // 배치 임베딩 (여러 텍스트 한 번에 처리)
    List<float[]> getEmbeddings(List<String> texts);
}
