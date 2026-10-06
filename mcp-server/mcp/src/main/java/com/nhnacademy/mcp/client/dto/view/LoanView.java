package com.nhnacademy.mcp.client.dto.view;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoanView {
    private Boolean hasBook = false;
    private Boolean loanAvailable = false;
}
