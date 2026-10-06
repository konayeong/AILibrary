package com.nhnacademy.agent.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.agent.dto.ChatResponse;
import com.nhnacademy.agent.dto.LibrarySearchParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AgentService {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public AgentService(@Qualifier("geminiChatClient") ChatClient chatClient,
                        ObjectMapper objectMapper) {
        this.chatClient = chatClient;
        this.objectMapper = objectMapper;
    }

    public ChatResponse chat(LibrarySearchParam param) {
        // 등록된 MCP 도구 호출
        try {
            String json =  chatClient.prompt()
                    .system("""
          당신은 MCP Tool 선택기입니다.
          사용자의 요청에 맞는 MCP tool을 선택하세요.

          [도구 선택 규칙]
          1. 반드시 등록된 MCP Tool을 사용해서 데이터를 조회
          2. 결과를 변경하거나 생성하지 말 것
          3. null은 실제 null로 처리
          
          [intent -> tool]
          - SEARCH_BOOK → 도서 검색
          - SEARCH_POPULARITY_BOOK → 인기 도서 (keyword : "인기", "인기도서", "많이 빌린", "대출 많은", "20대", "30대", "남성", "여성", "연령대", "지역별 인기")
          - SEARCH_LIBRARY → 도서관 검색 
          - SEARCH_LOAN_AVAILABLE → 대출 가능 여부
         
          [출력]
           {
             "resultType": "BOOK | LIBRARY | LOAN | KEYWORD | ERROR",
             "result": {...}
           }
           - Search Type : Error를 제외한 result는 항상 JSON 배열입니다.
        """)
            .user("""
        intent: %s

        [도서관 검색 조건]
        libraryCode: %s
        regionCode: %s
        dtlRegionCode: %s
                    
        [일반 도서 검색 조건]
        bookTitle: %s
        author: %s
        isbn: %s
        
        [인기도서 검색 조건]
        startDt: %s
        endDt: %s
        gender: %s
        fromAge: %s
        toAge: %s
        age: %s
        region: %s
        dtlRegion: %s
        bookDvsn: %s
        addCode: %s

        size: %s
        searchMonth: %s

        intent가 SEARCH_POPULARITY_BOOK이면 일반 도서 검색 Tool을 절대 호출하지 마세요.
        null 값은 문자열 "null"이 아니라 실제 null로 처리하세요.
        """.formatted(
        param.intent(),
        param.libraryCode(), param.regionCode(), param.dtlRegionCode(),
        param.bookTitle(), param.author(), param.isbn(),
        param.startDt(), param.endDt(), param.gender(), param.fromAge(), param.toAge(),
        param.age(), param.region(), param.dtlRegion(), param.bookDvsn(),
        param.addCode(),
        param.size(), param.searchMonth()
))
        .call()
        .content();

            return objectMapper.readValue(json, ChatResponse.class);
            // TODO-R Exception 처리 (무슨 에러인지 명확하게)
        }catch (Exception e) {
            log.error("Agent Failed", e);
            return ChatResponse.error("일시적인 오류가 발생했습니다.");
        }
    }
}
