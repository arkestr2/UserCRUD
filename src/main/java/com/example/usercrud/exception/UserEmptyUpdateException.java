package com.example.usercrud.exception;

public class UserEmptyUpdateException extends RuntimeException {
    public UserEmptyUpdateException() {
        super("User update cannot be empty");
    }
}
