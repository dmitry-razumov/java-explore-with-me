package ru.practicum.main_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    private static String stackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ApiError handleValidationException(ValidationException e) {
        log.error(String.format("Validation error: %s", e.getMessage()));
        return ApiError.of(
                HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(String.format("MethodArgumentNotValid error: %s", e.getMessage()));
        return ApiError.of(
                HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNotFoundException(NotFoundException e) {
        log.error(String.format("NotFound error: %s", e.getMessage()));
        return ApiError.of(
                HttpStatus.NOT_FOUND,
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(String.format("DataIntegrityViolation error: %s", e.getMessage()));
        return ApiError.of(
                HttpStatus.CONFLICT,
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiError handleForbiddenException(BadRequestException e) {
        log.error(String.format("Forbidden error: %s", e.getMessage()));
        return ApiError.of(
                HttpStatus.FORBIDDEN,
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConditionNotMetException.class)
    public ApiError handleConditionNotMetException(ConditionNotMetException e) {
        log.error(String.format("ConditionNotMetException error: %s", e.getMessage()));
        return ApiError.of(
                HttpStatus.FORBIDDEN,
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now());
    }
}
