package com.nhnacademy.mcp.client.dto.request;

import java.util.List;

public record PopularityBookSearchRequest(
        String startDt,
        String endDt,
        Integer gender,
        Integer fromAge,
        Integer toAge,
        List<Integer> age,
        List<Integer> region,
        List<Integer> dtlRegion,
        String bookDvsn,
        List<Integer> addCode
) {
}
