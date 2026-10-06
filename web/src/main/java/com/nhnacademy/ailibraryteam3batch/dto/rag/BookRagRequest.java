package com.nhnacademy.ailibraryteam3batch.dto.rag;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class BookRagRequest {
    private String isbn13;
    private String title;
    private String author;
    private String publisherName;
    @Setter
    private String content;
    private Double similarity;
}
