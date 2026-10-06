package com.nhnacademy.agent.service;

import com.nhnacademy.agent.dto.LibrarySearchParam;
import com.nhnacademy.agent.dto.MessageParamDto;
import com.nhnacademy.agent.util.LibraryLoader;
import com.nhnacademy.agent.util.RegionCodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 자연어 -> MessageParamDto
 */
@Slf4j
@Service
public class NatureLangParseService {
    private final ChatClient chatClient;
    private final LibraryLoader libraryCache;

    public NatureLangParseService(@Qualifier("messageParseChatClient") ChatClient chatClient,
                                  LibraryLoader libraryCache) {
        this.chatClient = chatClient;
        this.libraryCache = libraryCache;
    }

    public LibrarySearchParam parse(String message) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        String systemMessage = """
                    당신은 파라미터 추출기입니다.
                    값을 추측하지 않습니다.
                    입력되지 않은 값은 null 값으로 설정합니다.
                
                    시스템 컨텍스트 : 현재 날짜 {currentDate}
                    
                    추출 종류 및 방법:
                    - intent : 도구 선택용 intent, 예시: SEARCH_LIBRARY
                                                  SEARCH_BOOK
                                                  SEARCH_POPULARITY_BOOK
                                                  SEARCH_LOAN_AVAILABLE
                    - libraryName : 실제 도서관의 고유명칭
                    - regionName : 지역명(시/도), 예시: 서울, 광주, 부산, 제주 등
                    - dtlRegionName : 세부지역명(시/군/구), 예시: 남구, 제주시, 영월군 등
                    - bookTitle : 도서명
                    - author : 저자명
                    - isbn : 도서 ISBN
                    - size : 출력 데이터 수
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
                    - searchMonth : 검색 연/월 (yyyy-mm 형식)
                        * 주의 규칙 1: 이달의 키워드 등은 시스템 상 '직전월'까지만 조회가 가능합니다.
                        * 주의 규칙 2: 사용자가 '이번 달', '최신 트렌드'를 요구하더라도 조회 가능한 최신 데이터인 '직전월'로 변환하여 추출하세요.
                        * 주의 규칙 3: 사용자가 명시적인 날짜나 시간을 전혀 언급하지 않았다면 반드시 null로 설정하세요. (API가 자동으로 직전월로 처리합니다)
                    
                    인기 도서 검색이라면 지역명, 세부지역명을 regionName, dtlRegionName에 넣지말고 region, dtlRegion에 넣으세요.
                    그 외의 검색이라면 지역명, 세부지역명을 regionName, dtlRegionName에만 넣으세요.
                    예시:
                    "국립중앙도서관" -> libraryName:국립중앙도서관
                    "경기도 성남시 도서관 추천" -> regionName:경기도, dtlRegion:성남시
                    "위저드 베이커리라는 책 정보 좀" -> bookTitle:위저드 베이커리
                    "2026년 6월 이달의 키워드 알려줘" -> searchMonth:2026-06
                    "한강 책 추천해줘" or "한강 작가의 책 추천해줘" -> author:한강
                    "20대" -> age:20, fromAge:null, toAge:null
                    "20세에서 25세 사이 -> age:null, fromAge:20, toAge:25
                """;

        MessageParamDto paramDto = chatClient.prompt()
                .system(systemMessage)
                .system(promptSystemSpec -> promptSystemSpec
                        .text(systemMessage)
                        .param("currentDate", currentDate)
                )
                .user("""
                        입력된 자연어 메시지에서 필요한 파라미터를 추출하여 구조화된 DTO로 변환한다.
                        """)
                .user(message)
                .call()
                .entity(MessageParamDto.class);

        return normalize(paramDto);
    }

    // 코드로 변환
    private LibrarySearchParam normalize(MessageParamDto dto) {
        String regionCode = null;
        String libraryCode = null;
        String dtlRegionCode = null;

        if (dto.regionName() != null) {
            regionCode = RegionCodeMapper.regionCode(dto.regionName());
        }

        if (dto.dtlRegionName() != null) {
            dtlRegionCode = RegionCodeMapper.detailRegionCode(dto.dtlRegionName());

            if (dtlRegionCode == null && dto.regionName() != null) {
                // 지역명 + 세부 지역명으로 재확인
                dtlRegionCode = RegionCodeMapper.detailRegionCode(dto.regionName() + " " + dto.dtlRegionName());
            }
        }

        if (dto.libraryName() != null) {
            libraryCode = libraryCache.getCode(dto.libraryName());
        }
        List<Integer> regions = new ArrayList<>();
        if(dto.region()!=null&&!dto.region().isEmpty()){
            for(String region:dto.region()){
                regions.add(Integer.parseInt(RegionCodeMapper.regionCode(region)));
            }
        }

        List<Integer> dtlRegions = new ArrayList<>();
        if(dto.dtlRegion()!=null&&!dto.dtlRegion().isEmpty()){
            for(String dtlRegion:dto.dtlRegion()){
                dtlRegions.add(Integer.parseInt(RegionCodeMapper.detailRegionCode(dtlRegion)));
            }
        }

        Integer size = dto.size() != null ? dto.size() : 3;
        return new LibrarySearchParam(
                dto.intent(),
                libraryCode, regionCode, dtlRegionCode,
                dto.bookTitle(), dto.author(), dto.isbn(),
                size,
                dto.startDt(), dto.endDt(), dto.gender(), dto.fromAge(), dto.toAge(),
                dto.age(), regions, dtlRegions, dto.bookDvsn(),
                dto.addCode(),
                dto.searchMonth()
        );
    }
}
