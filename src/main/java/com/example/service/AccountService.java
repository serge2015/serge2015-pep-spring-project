package com.example.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AccountService {
    
    private AccountRepository accountRepository;
    private MessageService messageService;

    @Autowired
    public void AccountService(MessageService messageService, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.messageService = messageService;
    }

    public Account register(Account newAccount){
        Account registeredAccount = accountRepository.save(newAccount);
        return registeredAccount;
    }

    public Account login(String username, String password){
        Optional<Account> loggedinAccount = accountRepository.findByUsernameAndPassword(username, password);
        Account account = new Account();
        if (loggedinAccount.isPresent()){
            account = loggedinAccount.get();
            return account;
        }
        return null;
    }

    public Optional<Account> findByUsername(String username){
        return accountRepository.findByUsername(username);
    }
}
