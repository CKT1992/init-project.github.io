package com.example.backend.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import com.example.backend.entity.AccountModel;
import com.example.backend.entity.PwdErrorCounterModel;
import com.example.backend.exception.ValidateException;
import com.example.backend.repo.AccountRepository;
import com.example.backend.repo.PwdErrorCounterRepository;
import com.example.backend.service.AccountService;
import com.example.backend.service.ResponseService;
import com.example.backend.util.Password;
import com.example.backend.viewModel.ResponseFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private ResponseService responseService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PwdErrorCounterRepository pwdErrorCounterRepository;

    @Override
    public ResponseEntity<ResponseFormat> registerAccount(String firstName, String lastName, String birthday,
            String email, String password) {

        String accountId = UUID.randomUUID().toString();
        byte[] salt = Password.getSalt();
        AccountModel accountModel = new AccountModel();
        PwdErrorCounterModel pwdErrorCounterModel = new PwdErrorCounterModel();
        
        accountModel.setAccountId(accountId);
        accountModel.setFirstName(firstName);
        accountModel.setLastName(lastName);
        accountModel.setBirthday(birthday);
        accountModel.setEmail(email);
        accountModel.setPassword(Password.getSecurePassword(password, salt));
        accountModel.setSalt(salt);
        accountModel.setRole("user");
        //TODO: 認證信
        accountModel.setIsValid(true);
        accountModel.setCreateDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date().getTime()));
        accountModel.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date().getTime()));

        pwdErrorCounterModel.setAccountId(accountId);
        pwdErrorCounterModel.setCounter(0);

        accountRepository.save(accountModel);
        pwdErrorCounterRepository.save(pwdErrorCounterModel);

        return responseService.basic(HttpStatus.OK, "Success.", null);
    }

    @Override
    public Optional<AccountModel> loginValidation(String account, String password) {
        Optional<AccountModel> loginAccount = accountRepository.findByEmail(account);

        if(!loginAccount.isPresent()) {
			throw new ValidateException(HttpStatus.BAD_REQUEST, "The account does not exist.", null);
        }

        Optional<PwdErrorCounterModel> pwdErrorCounterModel = pwdErrorCounterRepository.findById(loginAccount.get().getAccountId());
        
        //如果沒資料，幫他建一個
        if(pwdErrorCounterModel.isPresent()) {
            //若錯誤超過3次，請洽詢服務台
            if(pwdErrorCounterModel.get().getCounter() >= 3) {
                throw new ValidateException(HttpStatus.FORBIDDEN, "Please contact customer service.", null);
            }
        } else {
            PwdErrorCounterModel newPwdErrorCounterModel = new PwdErrorCounterModel();
            newPwdErrorCounterModel.setAccountId(loginAccount.get().getAccountId());
            newPwdErrorCounterModel.setCounter(0);
            pwdErrorCounterRepository.save(newPwdErrorCounterModel);
            //重新載入
            // pwdErrorCounterModel = newPwdErrorCounterModel;
            pwdErrorCounterModel = pwdErrorCounterRepository.findById(loginAccount.get().getAccountId());
        }
        
        // 將輸入的密碼加密進行比對
        String hashPwd = Password.getSecurePassword(password, loginAccount.get().getSalt());
        if (!loginAccount.get().getPassword().equals(hashPwd)) {
            pwdErrorCounterModel.get().setCounter(pwdErrorCounterModel.get().getCounter()+1);
            pwdErrorCounterRepository.save(pwdErrorCounterModel.get());
            int countdown = 3 - pwdErrorCounterModel.get().getCounter();
            throw new ValidateException(HttpStatus.FORBIDDEN, "Password incorrect. You have " + countdown + " chances left.", null);
        } else {
            pwdErrorCounterModel.get().setCounter(0);
            pwdErrorCounterRepository.save(pwdErrorCounterModel.get());
        }
        return loginAccount;
    }

    @Override
    public Optional<AccountModel> getUser(String accountId) {
        Optional<AccountModel> accountModel = accountRepository.findById(accountId);
        if(!accountModel.isPresent()) {
			throw new ValidateException(HttpStatus.BAD_REQUEST, "The account does not exist.", null);
        }
		//TODO: 之後要把機密資訊拔除

        return accountModel;
    }

    @Override
    public void updateUser(AccountModel accountModel, AccountModel data) {
        accountModel.setFirstName(data.getFirstName());
        accountModel.setLastName(data.getLastName());
        accountModel.setPassword(Password.getSecurePassword(data.getPassword(), accountModel.getSalt()));
        accountModel.setRole(data.getRole());
        accountModel.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date().getTime()));
        
        accountRepository.save(accountModel);
    }
}
