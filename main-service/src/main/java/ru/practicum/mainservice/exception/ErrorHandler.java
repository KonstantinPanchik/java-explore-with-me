package ru.practicum.mainservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.mainservice.exception.model.ApiError;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleHappinessOverflow(final NotFoundException e) {

        ApiError apiError =
                ApiError.builder()
                        .reason("The required object was not found.")
                        .message(e.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .timestamp(LocalDateTime.now())
                        .build();


        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleHappinessOverflow(final FutureTwoHourException e) {

        ApiError apiError =
                ApiError.builder()
                        .reason("For the requested operation the conditions are not met.")
                        .message(e.getMessage())
                        .status(HttpStatus.FORBIDDEN)
                        .timestamp(LocalDateTime.now())
                        .build();


        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleHappinessOverflow(final ConflictException e) {

        ApiError apiError =
                ApiError.builder()
                        .reason("For the requested operation the conditions are not met.")
                        .message(e.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .timestamp(LocalDateTime.now())
                        .build();


        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }


    @ExceptionHandler
    public ResponseEntity<ApiError> handleHappinessOverflow(final ServletRequestBindingException e) {

        ApiError apiError =
                ApiError.builder()
                        .reason("Incorrectly made request.")
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(LocalDateTime.now())
                        .build();


        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleHappinessOverflow(final MethodArgumentNotValidException e) {

        ApiError apiError =
                ApiError.builder()
                        .reason("Incorrectly made request.")
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(LocalDateTime.now())
                        .build();


        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleHappinessOverflow(final Throwable e) {

        ApiError apiError =
                ApiError.builder()
                        .reason("For the requested operation the conditions are not met.")
                        .message(e.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .timestamp(LocalDateTime.now())
                        .build();


        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
