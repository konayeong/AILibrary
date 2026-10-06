package com.nhnacademy.ailibraryteam3batch.dto.rag;

import java.util.List;

public record RerankRequest(
        String query,
        List<RerankDocument> docs
) {

}
