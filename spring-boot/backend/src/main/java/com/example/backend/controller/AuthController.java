package com.example.backend.controller;

import com.example.backend.service.AuthService;
import com.example.backend.service.ResponseService;
import com.example.backend.viewModel.DecodeModel;
import com.example.backend.viewModel.LoginModel;
import com.example.backend.viewModel.ResponseFormat;
import com.example.backend.viewModel.TokenModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
@Api(tags = "Auth")
@ApiIgnore
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
	private ResponseService responseService;

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/encode")
    public ResponseEntity<ResponseFormat> tokenEncode(@RequestBody LoginModel data) {
        return null;
    }

    @PostMapping(value = "/decode")
    public ResponseEntity<ResponseFormat> tokenDecode(@RequestBody TokenModel data) {
        return responseService.basic(HttpStatus.OK, null, authService.decode(data.getToken()));
    }
}
