package com.nhnacademy.ailibraryteam3batch.service.book.impl;

import com.nhnacademy.ailibraryteam3batch.dto.embedding.BookEmbeddingTarget;
import com.nhnacademy.ailibraryteam3batch.repository.book.BookRepository;
import com.nhnacademy.ailibraryteam3batch.service.book.BookEmbeddingService;
import com.nhnacademy.ailibraryteam3batch.service.book.EmbeddingService;
import com.nhnacademy.ailibraryteam3batch.util.TextPreprocessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookEmbeddingServiceImpl implements BookEmbeddingService {
    private final BookRepository bookRepository;
    private final EmbeddingService embeddingService;


    @Override
    @Transactional
    public int generateEmbeddings() {
        List<BookEmbeddingTarget> books = bookRepository.findTop32ByEmbeddingIsNull();
        if (books.isEmpty()) {
            log.info("임베딩을 생성할 도서가 없습니다.");
            return 0;
        }
        log.info("임베딩 생성 시작: {}권", books.size());
        List<BookEmbeddingTarget> validBooks = new ArrayList<>();
        List<String> texts = new ArrayList<>();

        for (BookEmbeddingTarget book : books) {
            String combinedText = createCombinedText(book);

            if (combinedText != null && !combinedText.isBlank()) {
                validBooks.add(book);
                texts.add(combinedText);
            }
        }
        if (texts.isEmpty()) {
            log.warn("유효한 텍스트가 없습니다.");
            return 0;
        }

        List<float[]> embeddings = embeddingService.getEmbeddings(texts);

        for (int i = 0; i < validBooks.size(); i++) {
            BookEmbeddingTarget book = validBooks.get(i);
            float[] embedding = embeddings.get(i);
            bookRepository.updateEmbeddingById(book.id(), embedding);
            log.debug("임베딩 생성 완료: {} (차원: {})", book.title(), embedding.length);
        }
        log.info("임베딩 생성 완료: {}권", validBooks.size());
        return validBooks.size();
    }

    @Override
    public String createCombinedText(BookEmbeddingTarget book) {
        StringBuilder sb = new StringBuilder();

        // 제목
        if (book.title() != null) {
            String title = TextPreprocessor.preprocess(book.title());
            sb.append("[제목] ").append(title).append(" ");
        }

        // 저자
        if (book.author() != null) {
            String author = TextPreprocessor.preprocess(book.author());
            sb.append("[저자] ").append(author).append(" ");
        }

        // 내용
        if (book.content() != null) {
            String content = TextPreprocessor.preprocess(book.content());
            sb.append("[내용] ").append(content);
        }

        return sb.toString();
    }
}
