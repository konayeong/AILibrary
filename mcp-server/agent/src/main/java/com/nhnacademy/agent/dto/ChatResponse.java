package com.nhnacademy.agent.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nhnacademy.agent.dto.result.ResultType;

public record ChatResponse(
        ResultType resultType,
        JsonNode result
) {
    public static ChatResponse error(String message) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("message", message);
        return new ChatResponse(ResultType.ERROR, node);
    }
}
