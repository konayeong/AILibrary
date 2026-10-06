package com.nhnacademy.mcp.client.dto.view;

/**
 * 도서관 정보 조회
 */
public record LibraryView(
        String name,
        String address,
        String tel,
        String homepage,
        String closed,
        String operatingTime,
        Integer bookCount
) {}
