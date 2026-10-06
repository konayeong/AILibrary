package com.nhnacademy.librarytelegram.formatter.result;

public record LoanResult(
        String name,
        String address,
        String tel,
        String homepage,
        String closed,
        String operatingTime,

        Boolean hasBook,
        Boolean loanAvailable
) {
}
