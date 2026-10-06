package com.nhnacademy.mcp.tools;

import com.nhnacademy.mcp.client.BookClient;
import com.nhnacademy.mcp.client.LibraryClient;
import com.nhnacademy.mcp.client.dto.view.LibraryByBookInfoView;
import com.nhnacademy.mcp.client.dto.view.LibraryByLoanView;
import com.nhnacademy.mcp.client.dto.view.LoanView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanTool {
    private final LibraryClient libraryClient;
    private final BookClient bookClient;

    /**
     * 대출 가능 도서관 정보 검색
     */
    @McpTool(
            name = "search_book_available_libraries",
            description = """
                   현재 "대출 가능한" 도서관만 조회합니다.
                   "빌릴 수 있는 도서관", "대출 가능한 도서관" 등의 키워드가 들어간 문장은 이 Tool을 호출하세요. 
                   isbn값이 없는 경우 search_book tool을 호출하여 찾은 ISBN으로 다시 이 Tool을 호출하세요.
                    """
    )
    public List<LibraryByLoanView> searchLibraryByBorrow(
            @McpToolParam(description = "ISBN 13") String isbn,
            @McpToolParam(description = "지역코드") String regionCode,
            @McpToolParam(description = "세부지역코드", required = false) String dtlRegionCode,
            @McpToolParam(description = "조회할 도서관 개수") Integer size
    ) {
        log.info("[Library Tool] 대출 가능 도서관");
        List<LibraryByLoanView> result = new ArrayList<>();

        // 최대한 사용자가 원하는 개수 만큼 출력해주기 위해서 +5
        List<LibraryByBookInfoView> libraryByBookInfoViews = libraryClient.searchLibraryByBook(isbn, regionCode, dtlRegionCode, size + 5);

        for(LibraryByBookInfoView library : libraryByBookInfoViews) {
            if(result.size() == size) {
                log.info("[대출 가능 도서관 Tool] 응답값 완성 size : {}", result.size());
                break;
            }
            LoanView view = bookClient.searchLoanAvailable(library.libCode(), isbn);
            if(view.getLoanAvailable() && result.size() < size) {
                result.add(LibraryByLoanView.from(library, view));
            }
        }
        return result;
    }
}
