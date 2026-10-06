package com.nhnacademy.librarytelegram.formatter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.librarytelegram.dto.ChatResponse;
import com.nhnacademy.librarytelegram.formatter.result.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramFormatter {
    private final ObjectMapper mapper;

    public String format(ChatResponse response) {
        log.info("[Formatter] 출력 형식 : {}", response.resultType());
        return switch (response.resultType()) {
            case BOOK -> formatBook(response.result());
            case LIBRARY -> formatLibrary(response.result());
            case LOAN -> formatLoan(response.result());
            case KEYWORD -> formatKeyword(response.result());
            case ERROR -> formatError(response.result());
        };
    }

    private String formatBook(JsonNode result) {
        List<BookResult> bookResult = mapper.convertValue(result, new TypeReference<List<BookResult>>() {});

        StringBuilder sb = new StringBuilder();

        sb.append("📚 도서 검색 결과");

        for (BookResult book : bookResult) {
            sb.append("\n[").append(book.bookName()).append("] \n");
            sb.append("저자 : ").append(book.authors()).append('\n');
            sb.append("출판사 : ").append(book.publisher()).append('\n');
            sb.append("ISBN 13 : ").append(book.isbn13()).append('\n');
            sb.append("도서 상세 페이지 : ").append(book.bookDtlUrl()).append('\n');
            sb.append("대출횟수 : ").append(book.loanCount()).append("\n\n");
        }

        return sb.toString();
    }

    private String formatLibrary(JsonNode result) {
        List<LibraryResult> libraryResult = mapper.convertValue(result, new TypeReference<List<LibraryResult>>() {});

        StringBuilder sb = new StringBuilder();

        sb.append("🏢도서관 검색 결과");

        for (LibraryResult library : libraryResult) {
            sb.append("\n[").append(library.name()).append("] \n");
            sb.append("주소 : ").append(library.address())  .append('\n');
            sb.append("전화번호 : ").append(library.tel()).append('\n');
            sb.append("홈페이지 : ").append(library.homepage()).append('\n');
            sb.append("휴관일 : ").append(library.closed()).append('\n');
            sb.append("운영시간 : ").append(library.operatingTime()).append('\n');
            sb.append("단행본수 : ").append(library.bookCount() == null ? "-" : library.bookCount()).append('\n');
        }

        return sb.toString();
    }

    private String formatLoan(JsonNode result) {
        List<LoanResult> loanResult = mapper.convertValue(result, new TypeReference<List<LoanResult>>() {});

        StringBuilder sb = new StringBuilder();

        sb.append("💳대출 가능 도서관 검색 결과");

        for (LoanResult loan : loanResult) {
            sb.append("\n[").append(loan.name()).append("] \n");
            sb.append("주소 : ").append(loan.address())  .append('\n');
            sb.append("전화번호 : ").append(loan.tel()).append('\n');
            sb.append("홈페이지 : ").append(loan.homepage()).append('\n');
            sb.append("휴관일 : ").append(loan.closed()).append('\n');
            sb.append("운영시간 : ").append(loan.operatingTime()).append('\n');

            String hasBook = loan.hasBook() == true ? "소장 중" : "미소장";
            String loanAvailable = loan.loanAvailable() == true ? "대출 가능" : "대출 불가";

            sb.append("소장 여부 : ").append(hasBook).append('\n');
            sb.append("대출가능 여부 : ").append(loanAvailable).append('\n');
        }
        return sb.toString();
    }

    private String formatKeyword(JsonNode result) {
        List<MonthlyKeywordResult> keywordResult = mapper.convertValue(result, new TypeReference<List<MonthlyKeywordResult>>() {});

        StringBuilder sb = new StringBuilder();

        sb.append("[키워드]\n");

        for(MonthlyKeywordResult monthlyKeywordResult : keywordResult) {
            sb.append(monthlyKeywordResult.word()).append('\n');
        }

        return sb.toString();
    }

    private String formatError(JsonNode result) {
        ErrorResult errorResult = mapper.convertValue(result, ErrorResult.class);

        StringBuilder sb = new StringBuilder();

        sb.append("⛔오류 발생 \n\n");
        sb.append(errorResult.message());

        return sb.toString();
    }

}
