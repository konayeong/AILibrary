package com.nhnacademy.ailibraryteam3batch.dto.book;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public record BookRawData(
        Long id,                // 일련번호
        String isbn,    // ISBN 13자리
        String volumeName,              // 권 명
        String title,            // 도서명
        String author,            // 저자명
        String publisherName,        // 출판사명
        LocalDate firstPublishDate,       // 발행일
        String additionSymbol,      // 부가기호명
        Integer price,       // 가격
        String image,           // 이미지 URL
        String content,      // 도서 소개 내용
        String kdc,              // 한국십진분류명
        String subTitle,       // 대체 도서명
        String subAuthor,       // 대체 저자명
        LocalDate secondPublishDate,   // 2차 발행일
        Boolean isBookStorePresent,  // 인터넷 서점 도서 존재 여부
        Boolean isPortalPresent,   // 포털 사이트 도서 존재 여부
        String isbn10              // 구 ISBN
) {

    public static BookRawData from(CSVRecord record) {
        return new BookRawData(
                getLong(record, "SEQ_NO"),
                record.get("ISBN_THIRTEEN_NO"),
                record.get("VLM_NM"),
                record.get("TITLE_NM"),
                record.get("AUTHR_NM"),
                record.get("PUBLISHER_NM"),
                getDate(record, "PBLICTE_DE"),
                record.get("ADTION_SMBL_NM"),
                getInteger(record, "PRC_VALUE"),
                record.get("IMAGE_URL"),
                record.get("BOOK_INTRCN_CN"),
                record.get("KDC_NM"),
                record.get("TITLE_SBST_NM"),
                record.get("AUTHR_SBST_NM"),
                getDate(record, "TWO_PBLICTE_DE"),
                getBoolean(record, "INTNT_BOOKST_BOOK_EXST_AT"),
                getBoolean(record, "PORTAL_SITE_BOOK_EXST_AT"),
                record.get("ISBN_NO")
        );
    }

    private static String getString(CSVRecord record, String column) {
        String value = record.get(column);
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static Long getLong(CSVRecord record, String column) {
        String value = record.get(column);

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            String number = value.replaceAll("[^0-9]", "");

            if (number.isBlank()) {
                return null;
            }

            return Long.parseLong(number);

        } catch (Exception e) {
            log.warn("Long 파싱 실패 - column={}, value={}", column, value);
            return null;
        }
    }

    private static Integer getInteger(CSVRecord record, String column) {
        String value = record.get(column);

        if (value == null || value.isBlank()) {
            return null;
        }
        value = value.trim();

        try {
            return new BigDecimal(value).intValue();

        } catch (Exception e) {
            log.warn("정수 파싱 실패 - column={}, value={}", column, value);
            return null;
        }
    }

    private static Boolean getBoolean(CSVRecord record, String column) {
        String value = record.get(column);

        if (value == null || value.isBlank()) {
            return null;
        }

        return "Y".equalsIgnoreCase(value);
    }

    private static LocalDate getDate(CSVRecord record, String column) {
        String value = getString(record, column);
        if (value == null || value.isBlank()) {
            return null;
        }

        if (value.startsWith("民國")) {
            String number = value.replaceAll("[^0-9]", ""); // 790101
            int rocYear = Integer.parseInt(number.substring(0, number.length() - 4));
            int year = rocYear + 1911;

            String monthDay = number.substring(number.length() - 4);
            value = year + monthDay; // 19900101
        }

        String v = value.replaceAll("[^0-9]", "");


        if (v.length() == 4) {
            // 1998
            v = v + "0101";
        } else if (v.length() == 6) {
            // 199802
            v = v + "01";
        }
        if (v.length() == 8) {
            String year = v.substring(0, 4);
            String month = v.substring(4, 6);
            String day = v.substring(6, 8);

            // 19980000 같은 케이스
            if (month.equals("00")) {
                month = "01";
            }

            if (day.equals("00")) {
                day = "01";
            }

            v = year + month + day;

            // 19982000 같은 케이스
            if (!isValidDate(v)) {
                String firstYear = v.substring(0, 4);
                v = firstYear + "0101";
            }
        } else {
            return null;
        }

        return LocalDate.parse(
                v,
                DateTimeFormatter.ofPattern("uuuuMMdd")
        );
    }

    private static boolean isValidDate(String value) {
        try {
            LocalDate.parse(
                    value,
                    DateTimeFormatter.ofPattern("uuuuMMdd")
            );
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
