package com.nhnacademy.ailibraryteam3batch.dto.embedding;

public record BookEmbeddingTarget(
        Long id,
        String title,
        String author,
        String content
) {
}
