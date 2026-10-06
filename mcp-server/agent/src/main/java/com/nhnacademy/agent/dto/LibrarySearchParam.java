package com.nhnacademy.agent.dto;

import java.util.List;

// TODO-R 빌더패턴
public record LibrarySearchParam(
        String intent,

        String libraryCode,
        String regionCode,
        String dtlRegionCode,

        String bookTitle,
        String author,
        String isbn,

        Integer size,

        // 인기 대출 도서
        String startDt,
        String endDt,
        Integer gender,
        Integer fromAge,
        Integer toAge,
        List<Integer> age,
        List<Integer> region,
        List<Integer> dtlRegion,
        String bookDvsn,
        List<Integer> addCode,

        // 이달의 키워드
        String searchMonth
) {}
