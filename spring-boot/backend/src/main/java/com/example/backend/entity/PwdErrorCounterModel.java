package com.example.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "pwderrorcounter")
public class PwdErrorCounterModel {
    @Id
    @Column(name = "account_id")
    private String accountId;
    @ApiModelProperty(value = "If the error reaches three times, the account will be blocked")
    @Column(name = "counter")
    private Integer counter;
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public Integer getCounter() {
        return counter;
    }
    public void setCounter(Integer counter) {
        this.counter = counter;
    }
}
