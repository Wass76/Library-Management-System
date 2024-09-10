package com.BaseProject.utils.restExceptionHanding;

import com.BaseProject.utils.*;
import com.BaseProject.utils.exception.*;
import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class AbstractRestHandler {

    @ExceptionHandler(value = {RequestNotValidException.class})
    public ResponseEntity<Object> handleRequestNotValidException(RequestNotValidException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                LocalDateTime.now());
        return new ResponseEntity<>(apiException,badRequest);
    }

    @ExceptionHandler(value = {EmailTakenException.class})
    public ResponseEntity<Object> handleEmailTakenException(EmailTakenException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException,badRequest);
    }

    @ExceptionHandler(value = {ObjectNotFoundException.class})
    public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotValidException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException,badRequest);
    }

    @ExceptionHandler(value = {TooManyRequestException.class})
    public ResponseEntity<Object> handleTooManyRequestException(TooManyRequestException e){
        HttpStatus tooManyRequests = HttpStatus.TOO_MANY_REQUESTS;
        ApiException apiException = new ApiException(
                e.getMessage(),
                tooManyRequests,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException,tooManyRequests);
    }

    @ExceptionHandler(value = {UnAuthorizedException.class})
    public ResponseEntity<Object> handleUnauthorizedException(UnAuthorizedException e){
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        ApiException apiException = new ApiException(
                e.getMessage(),
                unauthorized,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException,unauthorized);
    }

}
