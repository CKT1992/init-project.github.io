package com.example.backend.viewModel;

public class LoginModel {
    //不排除手機登錄，所以先用account
    private String account;
    private String password;
    
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    
}
