package com.example.backend.controller;


import com.example.backend.exception.ValidateException;
import com.example.backend.model.ResponseFormat;
import com.example.backend.service.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@Api(tags = "Test")
@RequestMapping(path = "/api/v1/test", produces = { MediaType.APPLICATION_JSON_VALUE })
public class TestController {

    @Autowired
    private ResponseService responseService;
    

    @GetMapping(value = "response")
    public ResponseEntity<ResponseFormat> response() {
        return responseService.basic(HttpStatus.OK, "response test.", null);
    }

    @GetMapping(value = "exception")
    public ResponseEntity<ResponseFormat> exception() {
        throw new ValidateException(HttpStatus.BAD_REQUEST, "exception test.", null);
    }
}
