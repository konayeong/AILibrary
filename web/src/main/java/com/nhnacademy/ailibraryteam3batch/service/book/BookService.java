package com.nhnacademy.ailibraryteam3batch.service.book;

import com.nhnacademy.ailibraryteam3batch.dto.book.BookDetailResponse;

public interface BookService {
    BookDetailResponse getBookById(Long id);
}
