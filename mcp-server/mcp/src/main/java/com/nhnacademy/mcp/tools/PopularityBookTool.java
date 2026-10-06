package com.nhnacademy.mcp.tools;


import com.nhnacademy.mcp.client.PopularityBookClient;
import com.nhnacademy.mcp.client.dto.request.PopularityBookSearchRequest;
import com.nhnacademy.mcp.client.dto.view.BookView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PopularityBookTool {
    private final PopularityBookClient popularityBookClient;


    @McpTool(
            name = "search_popularity_book",
            description = """
                    인기 대출 도서 검색 도구입니다.
                    
                    - bookName(도서 이름)
                    - authors (저자)
                    - publisher (출판사)
                    - isbn13 (13자리 ISBN)
                    - bookDtlUrl (도서 상세 페이지 URL)
                    - loanCount (대출건수)
                    """
    )
    public List<BookView> searchPopularityBook(
            @McpToolParam(description = """
                    인기 대출 도서 검색 조건입니다.
                    
                    - startDt: 검색시작일자(대출기간). YYYY-MM-DD 형식
                    - endDt: 검색종료일자(대출기간). YYYY-MM-DD 형식
                    - gender: 성별코드(다중선택가능). 남자-> 0, 여자-> 1
                    - fromAge: 시작연령
                    - toAge: 종료연령
                    - age: 연령대 코드 목록
                    - region: 지역 코드 목록
                    - dtlRegion: 세부 지역 코드 목록
                    - bookDvsn: 도서 구분 ex) big: 큰글씨도서, oversea: 국외도서
                    - addCode: 부가기호 코드 목록

                    
                    값이 없으면 문자열 "null"이 아니라 실제 JSON null을 사용하세요.
                    복수 선택 값은 배열로 전달하세요.
                    예: "region": [11, 26]
                    코드를 모르면 임의로 추측하지 않습니다.
                    """)
        PopularityBookSearchRequest popularityBookSearchRequest
    ){
        log.info("[PopularityBook Tool] 인기 대출 도서 조회 호출");
        return popularityBookClient.search(popularityBookSearchRequest);
    }
}
