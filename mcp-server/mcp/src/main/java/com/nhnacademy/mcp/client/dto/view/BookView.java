package com.nhnacademy.mcp.client.dto.view;

public record BookView (
        String bookName,
        String authors,
        String publisher,
        String isbn13,
        String bookDtlUrl,
        Integer loanCount
){
}
