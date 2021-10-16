package com.example.backend.exception;

import org.springframework.http.HttpStatus;

public class ValidateException extends RuntimeException {

    private Long timestamp;
    private HttpStatus httpStatus;
    private String msg;
    private Object data;


    public ValidateException(HttpStatus httpStatus, String msg, Object data) {
        this.httpStatus = httpStatus;
        this.msg = msg;
        this.data = data;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }



    public Long getTimestamp() {
        return this.timestamp;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
