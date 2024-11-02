package com.eazybytes.accounts.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountsDto {

    @NotNull(message = "Account Number cannot be blank")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Account number must be 10 digits")
    private Long accountNumber;

    @NotEmpty(message = "Account Type cannot be empty")
    private String accountType;

    @NotEmpty(message = "Branch Address cannot be empty")
    private String branchAddress;

}
