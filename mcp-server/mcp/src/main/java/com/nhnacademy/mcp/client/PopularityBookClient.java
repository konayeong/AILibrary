package com.nhnacademy.mcp.client;


import com.nhnacademy.mcp.client.dto.request.PopularityBookSearchRequest;
import com.nhnacademy.mcp.client.dto.response.BookSearchResponse;
import com.nhnacademy.mcp.client.dto.view.BookView;
import com.nhnacademy.mcp.properties.NaruProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PopularityBookClient {
    private final NaruProperties properties;
    private final RestClient restClient;


    /**
     * 인기 대출 도서 조회
     * @param request 검색 파라미터
     * @return 도서 목록
     */
    public List<BookView> search(PopularityBookSearchRequest request){
        BookSearchResponse response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .path(properties.getPopularityBookSearch())
                            .queryParam("authKey", properties.getAuthKey())
                            .queryParam("format", "json");
                    if (hasText(request.startDt())) {
                        uriBuilder.queryParam("startDt", request.startDt());
                    }
                    if (hasText(request.endDt())) {
                        uriBuilder.queryParam("endDt", request.endDt());
                    }
                    if (request.gender() != null) {
                        uriBuilder.queryParam("gender", request.gender());
                    }
                    if (request.fromAge() != null) {
                        uriBuilder.queryParam("from_age", request.fromAge());
                    }
                    if (request.toAge() != null) {
                        uriBuilder.queryParam("to_age", request.toAge());
                    }
                    if (hasValues(request.age())) {
                        uriBuilder.queryParam("age", joinWithSemicolon(request.age()));
                    }
                    if (hasValues(request.region())) {
                        uriBuilder.queryParam("region", joinWithSemicolon(request.region()));
                    }
                    if (hasValues(request.dtlRegion())) {
                        uriBuilder.queryParam("dtl_region", joinWithSemicolon(request.dtlRegion()));
                    }
                    if (hasText(request.bookDvsn())) {
                        uriBuilder.queryParam("book_dvsn", request.bookDvsn());
                    }
                    if (hasValues(request.addCode())) {
                        uriBuilder.queryParam("addCode", joinWithSemicolon(request.addCode()));
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .body(BookSearchResponse.class);
        return response.response().docs()
                .stream()
                .map(doc -> new BookView(
                        doc.doc().bookname(),
                        doc.doc().authors(),
                        doc.doc().publisher(),
                        doc.doc().isbn13(),
                        doc.doc().bookDtlUrl(),
                        doc.doc().loan_count()
                ))
                .toList();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank() && !value.equalsIgnoreCase("null");
    }

    private boolean hasValues(List<Integer> values) {
        return values != null && !values.isEmpty();
    }

    private String joinWithSemicolon(List<Integer> values) {
        return values.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
    }
}
