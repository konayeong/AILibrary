package com.nhnacademy.agent.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * csv 파싱해서 초기 적재
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LibraryLoader implements ApplicationRunner {
    private final Map<String, String> cache = new HashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("도서관 이름 -> 코드 데이터 적재 작업 시작");

        ClassPathResource resource = new ClassPathResource("data/init/library.csv");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            br.readLine(); // header
            int count = 0;

            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",",2);

                if (cols.length < 2) continue;

                String name = cols[0].trim();
                String code = cols[1].trim();

                cache.put(name, code);
                count++;
            }

            log.info("도서관 데이터 적재 완료: {}건", count);

        } catch (Exception e) {
            log.error("도서관 CSV 로딩 실패", e);
        }
    }

    public String getCode(String name) {
        if(name == null || name.isBlank()) {
            return null;
        }

        // 완전 일치
        if(cache.containsKey(name)) {
            return cache.get(name);
        }
        // 가장 긴 키 우선
        String bestCode = null;
        int bestLength = 0;

        for (Map.Entry<String, String> entry : cache.entrySet()) {
            String key = entry.getKey();

            if (name.contains(key) || key.contains(name)) {
                if (key.length() > bestLength) {
                    bestLength = key.length();
                    bestCode = entry.getValue();
                }
            }
        }

        return bestCode;
    }
}

