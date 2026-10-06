package com.nhnacademy.librarytelegram.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record ChatResponse(
        ResultType resultType,
        JsonNode result
) {
}
