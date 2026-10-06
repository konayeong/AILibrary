package com.nhnacademy.librarytelegram.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ResultType {
    LIBRARY, BOOK, LOAN, KEYWORD, ERROR;

    @JsonCreator
    public static ResultType from(String v) {
        return ResultType.valueOf(v.toUpperCase());
    }
}
