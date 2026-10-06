package com.nhnacademy.mcp.tools;

import com.nhnacademy.mcp.client.MonthlyKeywordClient;
import com.nhnacademy.mcp.client.dto.view.MonthlyKeywordView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlyKeywordTool {

    private final MonthlyKeywordClient client;
    @McpTool(

            description = """
                    이달의 키워드(대출급상승 도서의 책 소개, 서평 등에서 추출된 단어)를 조회합니다.
                    
                    - word (단어)
                    - weight (가중치)
                    """
    )
    public List<MonthlyKeywordView> searchMonthlyKeyword(
            @McpToolParam(description = "검색월 (yyyy-mm 형식. 입력하지 않으면 검색일 기준 직전월 데이터 제공)") String month
    ) {
        log.info("[Library Tool] 이달의 키워드 조회 호출 (검색월: [{}]", month == null ? "기본값(직전월)" : month);
        return client.search(month);
    }
}
