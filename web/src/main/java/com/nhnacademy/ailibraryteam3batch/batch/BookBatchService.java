package com.nhnacademy.ailibraryteam3batch.batch;

import com.nhnacademy.ailibraryteam3batch.domain.book.Book;
import com.nhnacademy.ailibraryteam3batch.dto.book.BookRawData;
import com.nhnacademy.ailibraryteam3batch.repository.book.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * DTO -> Entity -> 저장
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookBatchService {
    private final BookRepository bookRepository;

    @Transactional
    public void saveBooks(List<BookRawData> rawDatas) {
        List<Book> books = rawDatas.stream().map(Book::from).toList();

        bookRepository.saveAll(books);
    }
}
