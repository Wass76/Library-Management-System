package com.BaseProject.utils.exception;

import lombok.Data;

@Data
public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String message)
    {super(message);}
}
