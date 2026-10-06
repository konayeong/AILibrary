package com.nhnacademy.mcp.client.dto.response;

import java.util.List;

/**
 * 이달의 키워드 API 응답 값
 * @param response
 */
public record MonthlyKeywordResponse(MonthlyKeywordResult response
) {
    public record MonthlyKeywordResult(
            Integer resultNum,
            List<KeywordWrapper> keywords
    ) {
        public record KeywordWrapper(
                Keyword keyword
        ) {
            public record Keyword(
                    String word,
                    Double weight
            ) {}
        }
    }
}
