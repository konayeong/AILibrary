package com.nhnacademy.ailibraryteam3batch.dto.rag;

public record BookRagAiResponse(
        String isbn,
        String reason,
        Integer relevance
) {
}
