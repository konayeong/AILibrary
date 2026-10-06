package com.nhnacademy.mcp.client.dto.view;

public record LibraryByBookInfoView (
        String libCode,
        String name,
        String address,
        String tel,
        String homepage,
        String closed,
        String operatingTime
){
}
