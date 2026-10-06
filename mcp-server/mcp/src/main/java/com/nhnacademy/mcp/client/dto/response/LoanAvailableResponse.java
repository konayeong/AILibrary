package com.nhnacademy.mcp.client.dto.response;

/**
 * 도서 소장여부 및 대출 가능여부 API 응답 값
 */
public record LoanAvailableResponse(
        LoanAvailableResult response
) {
    public record LoanAvailableResult(
            LoanAvailable result
    ){
        public record LoanAvailable(
                String hasBook,
                String loanAvailable
        ){}
    }
}
