package com.nhnacademy.ailibraryteam3batch.dto.rag;

public record BookRagResponse(
        Long id,
        String isbn,
        String title,
        String author,
        String publisherName,
        Integer price,
        String imageUrl,
        String content,
        String volumeName,
        Double similarity,
        Double rrfScore,
        Integer relevant
) {
}
