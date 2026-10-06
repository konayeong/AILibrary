package com.nhnacademy.mcp.client.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 도서관 정보 조회 API 응답 값
 * /libSrch, libSrchByBook
 */
public record LibrarySearchResponse(
        LibrarySearchResult response
) {
    public record LibrarySearchResult(
            Integer pageNo,
            Integer pageSize,
            Integer numFound,
            Integer resultNum,
            List<LibraryWrapper> libs
    ){
        public record LibraryWrapper(
                LibraryInfo lib
        ) {}

        public record LibraryInfo(
                String libCode,
                String libName,
                String address,
                String tel,
                String fax,
                Double latitude,
                Double longitude,
                String homepage,
                String closed,
                String operatingTime,
                @JsonProperty("BookCount")
                Integer bookCount
        ) {}
    }
}
