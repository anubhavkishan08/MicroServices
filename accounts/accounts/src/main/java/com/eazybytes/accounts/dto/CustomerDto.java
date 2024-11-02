package com.eazybytes.accounts.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {

    @NotNull(message = "Name cannot be blank")
    @Size(min = 2, message = "Name should have atleast 2 characters")
    private String name;

    @NotNull(message = "Email cannot be blank")
    @Email(message = "Invalid Email value")
    private String email;

    @Pattern(regexp = "([0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    private AccountsDto accountsDto;
}
