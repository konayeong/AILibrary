package com.nhnacademy.mcp.client;

import com.nhnacademy.mcp.client.dto.view.LibraryByBookInfoView;
import com.nhnacademy.mcp.client.dto.view.LibraryView;
import com.nhnacademy.mcp.client.dto.response.LibrarySearchResponse;
import com.nhnacademy.mcp.properties.NaruProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import java.util.List;

/**
 * HTTP 요청
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LibraryClient {

    private final NaruProperties properties;
    private final RestClient restClient;

    /**
     * 정보공개 도서관 조회
     */
    public List<LibraryView> search(String libraryCode, String regionCode, String dtlRegionCode, Integer size) {

        LibrarySearchResponse searchResponse = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(properties.getLibrarySearch());

                    commonQuery(uriBuilder, size);

                    addQuery(uriBuilder, "libCode", libraryCode);
                    addQuery(uriBuilder, "region", regionCode);
                    addQuery(uriBuilder, "dtl_region", dtlRegionCode);

                    return uriBuilder.build();
                })
                .retrieve()
                .body(LibrarySearchResponse.class);

        return searchResponse.response()
                .libs()
                .stream()
                .map(lib -> new LibraryView(
                        lib.lib().libName(),
                        lib.lib().address(),
                        lib.lib().tel(),
                        lib.lib().homepage(),
                        lib.lib().closed(),
                        lib.lib().operatingTime(),
                        lib.lib().bookCount()
                ))
                .toList();
    }

    /**
     * 도서 소장 도서관 조회
     */
    public List<LibraryByBookInfoView> searchLibraryByBook(String isbn, String regionCode, String dtlRegionCode, Integer size) {
        LibrarySearchResponse searchResponse = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(properties.getLibrarySearchByBook());

                    commonQuery(uriBuilder, size);

                    uriBuilder.queryParam("isbn", isbn);
                    addQuery(uriBuilder, "region", regionCode);
                    addQuery(uriBuilder, "dtl_region", dtlRegionCode);

                    return uriBuilder.build();
                })
                .retrieve()
                .body(LibrarySearchResponse.class);

        return searchResponse.response()
                .libs()
                .stream()
                .map(lib -> new LibraryByBookInfoView(
                        lib.lib().libCode(),
                        lib.lib().libName(),
                        lib.lib().address(),
                        lib.lib().tel(),
                        lib.lib().homepage(),
                        lib.lib().closed(),
                        lib.lib().operatingTime()
                ))
                .toList();
    }

    private void commonQuery(UriBuilder builder, Integer size) {
        builder.queryParam("authKey", properties.getAuthKey())
                .queryParam("format", "json")
                .queryParam("pageSize", size);
    }

    private void addQuery(UriBuilder builder, String name, String value) {
        if(value!= null && !value.isBlank() && !"null".equalsIgnoreCase(value)) {
            builder.queryParam(name, value);
        }
    }
}
