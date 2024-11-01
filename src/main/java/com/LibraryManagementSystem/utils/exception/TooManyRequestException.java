package com.LibraryManagementSystem.utils.exception;

import lombok.Data;

@Data
public class TooManyRequestException extends  RuntimeException{
    public TooManyRequestException(String message)
    {
        super(message);
    }
}