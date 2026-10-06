package com.nhnacademy.mcp.tools;

import com.nhnacademy.mcp.client.dto.view.LibraryByBookInfoView;
import com.nhnacademy.mcp.client.dto.view.LibraryView;
import com.nhnacademy.mcp.client.LibraryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LibraryTool {
    private final LibraryClient libraryClient;

    @McpTool(
            name = "search_library",
            description = """
                    도서관 검색만 담당합니다. (도서 정보를 조회하지 않습니다.)
                    """
    )
    public List<LibraryView> searchLibrary(
            @McpToolParam(description = "도서관 코드") String libraryCode,
            @McpToolParam(description = "지역코드") String regionCode,
            @McpToolParam(description = "세부지역코드") String dtlRegionCode,
            @McpToolParam(description = "조회할 도서관 개수") Integer size
    ) {
        log.info("[Library Tool] 도서관 정보 조회 호출");
        return libraryClient.search(libraryCode, regionCode, dtlRegionCode, size);
    }

    @McpTool(
            name = "search_library_have_book",
            description = """
                    도서를 "소장하고 있는" 도서관 정보를 조회합니다.
                    "가지고 있는 도서관", "소장하고 있는 도서관" 등의 키워드가 들어간 문장은 이 Tool을 호출하세요.
                    isbn값이 없는 경우 search_book tool을 호출하여 찾은 ISBN으로 다시 이 Tool을 호출하세요.
                    """
    )
    public List<LibraryByBookInfoView> searchLibraryByBook(
            @McpToolParam(description = "ISBN 13") String isbn,
            @McpToolParam(description = "지역코드") String regionCode,
            @McpToolParam(description = "세부지역코드", required = false) String dtlRegionCode,
            @McpToolParam(description = "조회할 도서관 개수") Integer size
    ) {
        log.info("[Library Tool] 도서 소장 도서관 정보 조회 호출");
        return libraryClient.searchLibraryByBook(isbn, regionCode, dtlRegionCode, size);
    }
}
