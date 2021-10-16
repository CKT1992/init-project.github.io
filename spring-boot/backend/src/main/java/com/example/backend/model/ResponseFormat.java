package com.example.backend.model;

import java.util.Date;

import org.springframework.http.HttpStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Response format")
public class ResponseFormat {

    @ApiModelProperty(value = "Current timestamp.")
    private Long timestamp = new Date().getTime();
    @ApiModelProperty(value = "Http status.")
    private HttpStatus httpStatus;
    @ApiModelProperty(value = "Returned massage.")
    private String msg;
    @ApiModelProperty(value = "Returned data.")
    private Object data;


    public Long getTimestamp() {
        return this.timestamp;
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
