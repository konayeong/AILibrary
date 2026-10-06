package com.nhnacademy.mcp.client;

import com.nhnacademy.mcp.client.dto.response.MonthlyKeywordResponse;
import com.nhnacademy.mcp.client.dto.view.MonthlyKeywordView;
import com.nhnacademy.mcp.properties.NaruProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlyKeywordClient {

    private final NaruProperties properties;
    private final RestClient restClient;

    // View DTO 만들지 말고 그냥 String만 내보낼지 고민중
    public List<MonthlyKeywordView> search(String month) {
        MonthlyKeywordResponse response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .path(properties.getMonthlyKeyword())
                            .queryParam("authKey", properties.getAuthKey())
                            .queryParam("month", month)
                            .queryParam("format", "json");
                    return uriBuilder.build();
                })
                .retrieve()
                .body(MonthlyKeywordResponse.class);

        // 가중치 10 이상의 키워드만 내보냄
        return response.response().keywords()
                .stream()
                .filter(keyword -> keyword.keyword().weight() > 10.0)
                .map(keyword -> new MonthlyKeywordView(
                        keyword.keyword().word()
                ))
                .toList();
    }

}
