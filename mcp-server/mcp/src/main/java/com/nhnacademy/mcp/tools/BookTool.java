package com.nhnacademy.mcp.tools;

import com.nhnacademy.mcp.client.dto.view.BookView;
import com.nhnacademy.mcp.client.dto.view.LoanView;
import com.nhnacademy.mcp.client.BookClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookTool {
    private final BookClient bookClient;

    @McpTool(
            name = "search_book",
            description = """
                    도서 검색만 담당합니다. (도서관 정보를 조회하지 않습니다. 인기 등 그런 조건은 고려하지 않고 오직 도서 검색만 담당합니다.)
                    """
    )
    public List<BookView> searchBook(
            @McpToolParam(description = "도서명") String bookTitle,
            @McpToolParam(description = "저자명") String authors,
            @McpToolParam(description = "조회할 도서 개수") Integer size
    ) {
        log.info("[Book Tool] 도서 정보 조회 호출");
        return bookClient.search(bookTitle, authors, size);
    }

//    @McpTool(
//            name = "search_loan_available",
//            description = """
//                    도서관별 도서 소장여부 및 대출 가능여부를 조회합니다.
//                    isbn값이 없는 경우 search_book tool을 호출하여 그 ISBN으로 다시 이 Tool을 호출하세요
//
//                    libraryCode가 없는 경우 searchLibraryByBook를 사용하여 도서관 코드를 가져오세요.
//                    """
//    )
//    public LoanView searchLoanAvailable(
//            @McpToolParam(description = "도서관 코드 (지역 코드가 아님)") String libraryCode,
//            @McpToolParam(description = "isbn") String isbn) {
//        log.info("[Book Tool] 도서 소장/대출 가능 여부 조회 호출");
//        return bookClient.searchLoanAvailable(libraryCode, isbn);
//    }
}
