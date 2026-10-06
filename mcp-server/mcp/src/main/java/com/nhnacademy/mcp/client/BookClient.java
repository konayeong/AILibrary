package com.nhnacademy.mcp.client;

import com.nhnacademy.mcp.client.dto.view.BookView;
import com.nhnacademy.mcp.client.dto.view.LoanView;
import com.nhnacademy.mcp.client.dto.response.BookSearchResponse;
import com.nhnacademy.mcp.client.dto.response.LoanAvailableResponse;
import com.nhnacademy.mcp.properties.NaruProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookClient {
    private final NaruProperties properties;
    private final RestClient restClient;

    /**
     * 도서 조회
     */
    public List<BookView> search(String bookTitle, String authors, Integer size) {
        BookSearchResponse response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .path(properties.getBookSearch())
                            .queryParam("authKey", properties.getAuthKey())
                            .queryParam("format", "json")
                            .queryParam("pageSize", size);

                    if(bookTitle != null && !bookTitle.isBlank() && !bookTitle.equalsIgnoreCase("null")) {
                        uriBuilder.queryParam("title", bookTitle);
                    }

                    if(authors != null && !authors.isBlank() && !authors.equalsIgnoreCase("null")) {
                        uriBuilder.queryParam("author", authors);
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

    /**
     * 도서 소장, 대출 가능 여부 조회
     */
    public LoanView searchLoanAvailable(String libraryCode, String isbn) {
        LoanAvailableResponse response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(properties.getBookExist())
                            .queryParam("authKey", properties.getAuthKey())
                            .queryParam("format", "json")
                            .queryParam("libCode", libraryCode)
                            .queryParam("isbn13",isbn);
                    log.info("uri : {}", uriBuilder.build());
                    return uriBuilder.build();
                })
                .retrieve()
                .body(LoanAvailableResponse.class);

        log.info("response : {}", response);
        LoanView view = new LoanView();
        if("Y".equalsIgnoreCase(response.response().result().hasBook())) {
            view.setHasBook(true);
        }

        if("Y".equalsIgnoreCase(response.response().result().loanAvailable())) {
            view.setLoanAvailable(true);
        }

        return view;
    }
}
