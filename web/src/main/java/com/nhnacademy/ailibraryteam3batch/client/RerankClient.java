package com.nhnacademy.ailibraryteam3batch.client;

import com.nhnacademy.ailibraryteam3batch.dto.rag.RerankDocument;
import com.nhnacademy.ailibraryteam3batch.dto.rag.RerankRequest;
import com.nhnacademy.ailibraryteam3batch.dto.rag.RerankResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class RerankClient {

    private final WebClient webClient;

    public RerankClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8000")
                .build();
    }

    public List<RerankResponse> rerank(String query, List<RerankDocument> docs) {

        RerankRequest request = new RerankRequest(query, docs);

        return webClient.post()
                .uri("/rerank")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(RerankResponse.class)
                .collectList()
                .block();
    }
}
