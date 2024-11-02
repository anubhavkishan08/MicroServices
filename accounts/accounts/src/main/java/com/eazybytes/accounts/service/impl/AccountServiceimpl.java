package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.Exceptions.CustomerAlreadyExistException;
import com.eazybytes.accounts.Exceptions.ResourceNotFoundException;
import com.eazybytes.accounts.constants.AccountConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceimpl implements IAccountService {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;

    //due to single argument constructor in this class,
    // Spring will inject the dependencies,so autowire is not needed.

    /**
     * @param customerDto
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistException("Customer Already Exist with Mobile Number=" + " "
                    + customer.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anubhav");
        Customer savedCustomer = customerRepository.save(customer);

        accountRepository.save(creteNewAccount(savedCustomer));
    }


    private Accounts creteNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long ac_num = 10000000L + new Random().nextLong(10000000L);
        newAccount.setAccountNumber(ac_num);
        newAccount.setAccountType(AccountConstants.SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Anubhav");
        return newAccount;
    }


    /**
     * @param //mobile_Number
     * @return
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {

        Optional<Customer> customer = customerRepository.findByMobileNumber(mobileNumber);

        if (customer.isPresent()) {

            Optional<Accounts> account = accountRepository.findByCustomerId(customer.get().getCustomerId());

            if (account.isPresent()) {
                // Map customer to DTO and return
                CustomerDto customerDto=CustomerMapper.mapToCustomerDto(new CustomerDto(),customer.get());
                customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account.get(),new AccountsDto()));
                return customerDto;
            } else {
                throw new ResourceNotFoundException("Account", "customerId", customer.get().getCustomerId().toString());
            }
        } else {
            throw new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber);
        }

    }

    /**
     * @param //customerDto
     * @return
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccount(accounts, accountsDto);
            accounts = accountRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }
//    @Override
//    public CustomerDto fetchAccount(String mobileNumber) {
//        Customer customer=customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
//                ()->new ResourceNotFoundException("Customer","mobileNumber",mobileNumber)
//        );
//        Accounts accounts=accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
//                ()->new ResourceNotFoundException("Account","customerId",customer.getCustomerId().toString())
//        );
//        return null;
//    }
}
