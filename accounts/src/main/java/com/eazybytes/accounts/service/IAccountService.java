package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDto;


public interface IAccountService {

    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobile_Number);

    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);
}
