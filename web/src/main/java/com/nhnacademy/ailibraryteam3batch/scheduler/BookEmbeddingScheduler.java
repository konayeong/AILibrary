package com.nhnacademy.ailibraryteam3batch.scheduler;

import com.nhnacademy.ailibraryteam3batch.service.book.BookEmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookEmbeddingScheduler {
    private final BookEmbeddingService bookEmbeddingService;

//    @Scheduled(fixedDelay = 5000)
    public void generateEmbeddings() {
        try {
            int count = bookEmbeddingService.generateEmbeddings();

            if (count > 0) {
                log.info("{}권의 도서 임베딩 생성 완료", count);
            }
        }catch (Exception e){
            log.error("임베딩 생성 중 오류 발생", e);
        }
    }
}
