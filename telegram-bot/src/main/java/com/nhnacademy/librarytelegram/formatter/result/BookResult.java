package com.nhnacademy.librarytelegram.formatter.result;

public record BookResult(
        String bookName,
        String authors,
        String publisher,
        String isbn13,
        String bookDtlUrl,
        Integer loanCount
){
}
