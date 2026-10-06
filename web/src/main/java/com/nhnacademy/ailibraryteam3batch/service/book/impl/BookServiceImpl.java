package com.nhnacademy.ailibraryteam3batch.service.book.impl;

import com.nhnacademy.ailibraryteam3batch.domain.book.Book;
import com.nhnacademy.ailibraryteam3batch.dto.book.BookDetailResponse;
import com.nhnacademy.ailibraryteam3batch.exception.BookNotFoundException;
import com.nhnacademy.ailibraryteam3batch.repository.book.BookRepository;
import com.nhnacademy.ailibraryteam3batch.service.book.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public BookDetailResponse getBookById(Long id) {
        log.info("도서 상세 조회 : {}", id);

        Optional<Book> book = bookRepository.findById(id);

        if(book.isEmpty()) {
            log.info("[도서 조회 실패] 존재하지 않는 도서 : {}", id);
            throw new BookNotFoundException(id);
        }
        return BookDetailResponse.from(book.get());
    }
}
