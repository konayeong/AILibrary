package com.nhnacademy.mcp.client.dto.response;

import java.util.List;

/**
 * 도서 정보 조회 API 응답 값
 */
public record BookSearchResponse (
        BookSearchResult response
){
    public record BookSearchResult(
            Integer numFound,
            List<BookWrapper> docs
    ) {
        public record BookWrapper(
                BookInfo doc
        ) {
            public record BookInfo(
                    String bookname,
                    String authors,
                    String publisher,
                    String publication_year,
                    String isbn13,
                    String addition_symbol,
                    String vol,
                    String class_no,
                    String class_nm,
                    String bookImageUrl,
                    String bookDtlUrl,
                    Integer loan_count
            ){}
        }
    }
}
