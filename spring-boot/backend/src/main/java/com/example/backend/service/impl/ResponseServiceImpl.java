package com.example.backend.service.impl;

import com.example.backend.exception.ValidateException;
import com.example.backend.model.ResponseFormat;
import com.example.backend.service.ResponseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResponseServiceImpl implements ResponseService{

    private static Logger logger = LoggerFactory.getLogger(ValidateException.class);

    @Override
    public ResponseEntity<ResponseFormat> basic(HttpStatus httpStatus, String msg, Object data) {
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setHttpStatus(httpStatus);
        responseFormat.setMsg(msg);
        responseFormat.setData(data);
        return ResponseEntity.status(httpStatus).body(responseFormat);
    }

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<ResponseFormat> validateException(ValidateException ex) {
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setHttpStatus(ex.getHttpStatus());
        responseFormat.setMsg(ex.getMsg());
        responseFormat.setData(ex.getData());
        logger.error("[" + ex.getHttpStatus() + "] " + ex.getMsg());
        return ResponseEntity.status(ex.getHttpStatus()).body(responseFormat);
    }
    
}
