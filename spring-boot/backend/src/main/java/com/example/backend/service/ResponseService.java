package com.example.backend.service;

import com.example.backend.viewModel.ResponseFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ResponseService {
    public ResponseEntity<ResponseFormat> basic(HttpStatus httpStatus, String msg, Object data);
}
