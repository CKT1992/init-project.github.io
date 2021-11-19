package com.example.backend.service;

import java.util.Optional;

import com.example.backend.entity.AccountModel;
import com.example.backend.viewModel.ResponseFormat;

import org.springframework.http.ResponseEntity;

public interface AccountService {
    // 註冊帳號
    public ResponseEntity<ResponseFormat> registerAccount(String firstName, String lastName, String birthday,
            String email, String password);
    //登入驗證
    public Optional<AccountModel> loginValidation(String account, String password);
    public Optional<AccountModel> getUser(String accountId);
    public void updateUser(AccountModel accountModel, AccountModel data);
}
