package com.nhnacademy.mcp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("naru")
public class NaruProperties {
    private String baseUrl;
    private String authKey;

    private String librarySearch; // 도서관 조회
    private String bookSearch; // 도서 조회
    private String librarySearchByBook; // 도서 소장 도서관 조회
    private String popularityBookSearch; // 인기 대출 도서 조회
    private String bookExist; // 도서 소장/대출
    private String monthlyKeyword;  // 이달의 키워드
}
