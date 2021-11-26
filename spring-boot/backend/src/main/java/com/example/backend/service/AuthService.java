package com.example.backend.service;

import com.example.backend.viewModel.DecodeModel;
import com.example.backend.viewModel.TokenModel;


public interface AuthService {
    //帳號登入之後回傳token (解出來)
    public TokenModel encode(String accountId, Long loginTime); 
    //token解析以回傳個人資訊
    public DecodeModel decode(String token);
}