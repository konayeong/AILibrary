package com.nhnacademy.ailibraryteam3batch.batch;

import com.nhnacademy.ailibraryteam3batch.dto.book.BookRawData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV 파싱
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CsvBookParser {
    private static final int CHUNK_SIZE = 1000;
    private final BookBatchService bookBatchService;

    public void parse(String filePath) throws IOException {
        long startTime = System.nanoTime();
        // CsvBookParser(singleton) -> chunk가 객체상태가 되지 않도록 지역 변수로
        List<BookRawData> chunk = new ArrayList<>(CHUNK_SIZE);
        log.info("도서 CSV 파싱 시작 : {}", filePath);

        // 파일 한 개 읽어오기
        ClassPathResource resource = new ClassPathResource(filePath);

        CSVFormat format = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .build();

        try (BOMInputStream bis = BOMInputStream.builder().setInputStream(resource.getInputStream()).get();
             InputStreamReader reader = new InputStreamReader(bis, StandardCharsets.UTF_8);
             CSVParser parser = format.parse(reader)) {
            int totalCnt = 0;

            // parser.getRecords() 사용 시 chunk 전략 무의미 -> 스트리밍 처리
            for(CSVRecord record : parser) {
                chunk.add(BookRawData.from(record));

                if(chunk.size() >= CHUNK_SIZE) {
                    bookBatchService.saveBooks(chunk);
                    totalCnt += chunk.size();
                    log.info("{}권 파싱 완료", chunk.size());
                    chunk.clear();
                }
            }
            if(!chunk.isEmpty()) {
                bookBatchService.saveBooks(chunk);
                totalCnt += chunk.size();
            }
            long elapsedMs = (System.nanoTime() - startTime) / 1_000_000;

            // 전체 파싱 완료
            log.info(
                    "총 {}권 파싱 완료 (소요시간: {} ms, 약 {}초)",
                    totalCnt,
                    elapsedMs,
                    elapsedMs / 1000.0
            );
         }
    }
}
