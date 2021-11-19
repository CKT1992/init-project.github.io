package com.example.backend.controller;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.example.backend.entity.AccountModel;
import com.example.backend.exception.ValidateException;
import com.example.backend.repos.AccountRepository;
import com.example.backend.service.AccountService;
import com.example.backend.service.AuthService;
import com.example.backend.service.ResponseService;
import com.example.backend.util.ValidationUtil;
import com.example.backend.viewModel.DecodeModel;
import com.example.backend.viewModel.LoginModel;
import com.example.backend.viewModel.ResponseFormat;
import com.example.backend.viewModel.TokenModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
@Api(tags = "Account")
@RequestMapping(path = "/api/v1/account", produces = { MediaType.APPLICATION_JSON_VALUE })
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthService authService;

	@Autowired
	private ResponseService responseService;

	@Autowired
	private AccountRepository accountRepository;

	@ApiOperation(value = "Sign up.", notes = "")
	@PostMapping(value = "/register")
	public ResponseEntity<ResponseFormat> createAccount(@RequestBody AccountModel data) {
		if (data.getEmail() == null || !ValidationUtil.isValidEmail(data.getEmail())) {
			throw new ValidateException(HttpStatus.UNPROCESSABLE_ENTITY, "Email format error.", null);
		}

		if (accountRepository.findByEmail(data.getEmail()).isPresent()) {
			throw new ValidateException(HttpStatus.CONFLICT, "Account already exist.", null);
		}

		if (data.getFirstName() == null || data.getFirstName().isEmpty()) {
			throw new ValidateException(HttpStatus.UNPROCESSABLE_ENTITY, "First name invalid.", null);
		}

		if (data.getLastName() == null || data.getLastName().isEmpty()) {
			throw new ValidateException(HttpStatus.UNPROCESSABLE_ENTITY, "Last name invalid.", null);
		}

		if (data.getBirthday() == null || !ValidationUtil.isValidDateFormat(data.getBirthday())) {
			throw new ValidateException(HttpStatus.UNPROCESSABLE_ENTITY, "Birthday format invalid.  ex. 1994-01-20",
					null);
		}

		if (data.getPassword() == null || !ValidationUtil.isValidPassword(data.getPassword())) {
			throw new ValidateException(HttpStatus.UNPROCESSABLE_ENTITY, "Password invalid.", null);
		}

		return accountService.registerAccount(data.getFirstName(), data.getLastName(), data.getBirthday(),
				data.getEmail(), data.getPassword());

	}

	@ApiOperation(value = "Login.", notes = "")
	@PostMapping(value = "/login")
	public ResponseEntity<ResponseFormat> login(@RequestBody LoginModel data) {
		if (data.getAccount() == null || !ValidationUtil.isValidEmail(data.getAccount())) {
			throw new ValidateException(HttpStatus.UNPROCESSABLE_ENTITY, "Email format error.", null);
		}
		Optional<AccountModel> accountModel = accountService.loginValidation(data.getAccount(), data.getPassword());
		TokenModel tokenModel = authService.encode(accountModel.get().getAccountId(), new Date().getTime());
		//TODO: 要記錄登入時間/哪邊登入/誰登入

		return responseService.basic(HttpStatus.OK, null, tokenModel);
	}

	// --------------------- 以下api都要用token換資料 --------------------- //

	@ApiOperation(value = "self information.", notes = "")
	@GetMapping(value = "/me")
	public ResponseEntity<ResponseFormat> readAccount(HttpServletRequest httpRequest) {
		if(!ValidationUtil.isValidToken(httpRequest.getHeader("Authorization"))) {
			//TODO: 要做強制登出
			throw new ValidateException(HttpStatus.FORBIDDEN, "Token format error.", null);
		}

		DecodeModel decodeModel = authService.decode(httpRequest.getHeader("Authorization"));
		Optional<AccountModel> accountModel = accountService.getUser(decodeModel.getAccountId());

		return responseService.basic(HttpStatus.OK, null, accountModel);
	}

	@ApiOperation(value = "edit personal information.", notes = "")
	@PatchMapping(value = "/me")
	public ResponseEntity<ResponseFormat> updateAccount(HttpServletRequest httpRequest, @RequestBody AccountModel data) {
		if(!ValidationUtil.isValidToken(httpRequest.getHeader("Authorization"))) {
			//TODO: 要做強制登出
			throw new ValidateException(HttpStatus.FORBIDDEN, "Token format error.", null);
		}

		DecodeModel decodeModel = authService.decode(httpRequest.getHeader("Authorization"));
		Optional<AccountModel> accountModel = accountService.getUser(decodeModel.getAccountId());

		if (!accountModel.get().getEmail().equals(data.getEmail())) {
			throw new ValidateException(HttpStatus.UNPROCESSABLE_ENTITY, "The email cannot be changed, please contact the service staff.", null);
		}

		if (data.getFirstName() == null || data.getFirstName().isEmpty()) {
			throw new ValidateException(HttpStatus.UNPROCESSABLE_ENTITY, "First name invalid.", null);
		}

		if (data.getLastName() == null || data.getLastName().isEmpty()) {
			throw new ValidateException(HttpStatus.UNPROCESSABLE_ENTITY, "Last name invalid.", null);
		}

		if (data.getBirthday() == null || !ValidationUtil.isValidDateFormat(data.getBirthday())) {
			throw new ValidateException(HttpStatus.UNPROCESSABLE_ENTITY, "Birthday cannot be changed, please contact the service staff.",
					null);
		}

		if (data.getPassword() == null || !ValidationUtil.isValidPassword(data.getPassword())) {
			throw new ValidateException(HttpStatus.UNPROCESSABLE_ENTITY, "Password invalid.", null);
		}

		accountService.updateUser(accountModel.get(), data);

		return responseService.basic(HttpStatus.OK, "Success.", null);
	}

	//TODO: 不同權限看到的不一樣
	@ApiOperation(value = "account list.", notes = "")
	@GetMapping(value = "/list")
	public ResponseEntity<ResponseFormat> getAccountList(HttpServletRequest httpRequest) {
		if(!ValidationUtil.isValidToken(httpRequest.getHeader("Authorization"))) {
			//TODO: 要做強制登出
			throw new ValidateException(HttpStatus.FORBIDDEN, "Token format error.", null);
		}
		return responseService.basic(HttpStatus.OK, null, accountRepository.findAll());
	}

	@ApiOperation(value = "delete account", notes = "")
	@DeleteMapping(value = "/delete")
	public ResponseEntity<ResponseFormat> deleteAccount(HttpServletRequest httpRequest, @RequestParam(value = "id") String id) {
		if(!ValidationUtil.isValidToken(httpRequest.getHeader("Authorization"))) {
			//TODO: 要做強制登出
			throw new ValidateException(HttpStatus.FORBIDDEN, "Token format error.", null);
		}
		DecodeModel decodeModel = authService.decode(httpRequest.getHeader("Authorization"));

		if(!decodeModel.getAccountId().equals(id)) {
			throw new ValidateException(HttpStatus.FORBIDDEN, "Process denied.", null);
		}

		if(!accountRepository.findById(id).isPresent()) {
			throw new ValidateException(HttpStatus.BAD_REQUEST, "The account does not exist.", null);
		}
			accountRepository.deleteById(id);
		return responseService.basic(HttpStatus.OK, null, null);
	}
}
