package com.nhnacademy.librarytelegram.formatter.result;

public record LibraryResult(
        String name,
        String address,
        String tel,
        String homepage,
        String closed,
        String operatingTime,
        Integer bookCount
) {}
