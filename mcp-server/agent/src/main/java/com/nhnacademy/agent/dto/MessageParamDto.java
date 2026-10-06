package com.nhnacademy.agent.dto;

import java.util.List;

/**
 * 자연어 기반 데이터
 */
public record MessageParamDto(
        String intent,

        // 도서관
        String libraryName,
        String regionName,
        String dtlRegionName,

        // 도서
        String bookTitle,
        String author,
        String isbn,

        // 몇 개
        Integer size,

        String startDt,
        String endDt,
        Integer gender,
        Integer fromAge,
        Integer toAge,
        List<Integer> age,
        List<String> region,
        List<String> dtlRegion,
        String bookDvsn,
        List<Integer> addCode,

        String searchMonth

) {
}
