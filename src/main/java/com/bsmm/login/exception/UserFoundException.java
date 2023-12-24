package com.bsmm.login.exception;

public class UserFoundException extends RuntimeException {
    public UserFoundException(String sms) {
        super(sms);
    }
}
