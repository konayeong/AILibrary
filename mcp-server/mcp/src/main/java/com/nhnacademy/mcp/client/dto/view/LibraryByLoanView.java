package com.nhnacademy.mcp.client.dto.view;

/**
 * 대출 가능 도서관 정보 + 소유/대출 정보
 */
public record LibraryByLoanView (
        String name,
        String address,
        String tel,
        String homepage,
        String closed,
        String operatingTime,

        Boolean hasBook,
        Boolean loanAvailable
) {
    public static LibraryByLoanView from(LibraryByBookInfoView library, LoanView loan) {
        return new LibraryByLoanView(
                library.name(),
                library.address(),
                library.tel(),
                library.homepage(),
                library.closed(),
                library.operatingTime(),
                loan.getHasBook(),
                loan.getLoanAvailable()
        );
    }
}
