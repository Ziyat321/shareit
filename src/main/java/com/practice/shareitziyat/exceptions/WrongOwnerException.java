package com.practice.shareitziyat.exceptions;

public class WrongOwnerException extends RuntimeException{
    public WrongOwnerException(String message) {
        super(message);
    }
}
