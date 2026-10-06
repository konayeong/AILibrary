package com.nhnacademy.ailibraryteam3batch.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/init")
public class BookInitController {
    private volatile boolean running = false;
    private final CsvBookParser csvBookParser;

    @PostMapping("/books")
    public synchronized ResponseEntity<String> initBooks() throws IOException {

        if (running) {
            return ResponseEntity.status(409).body("Already running");
        }

        running = true;
        try {
            csvBookParser.parse("data/init/BOOK_DB_202112.csv");
            return ResponseEntity.ok("completed");
        } finally {
            running = false;
        }
    }
}
