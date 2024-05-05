package ru.practicum.main_service.exception;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Value(staticConstructor = "of")
public class ApiError {
    HttpStatus status;
    String reason;
    String message;
    LocalDateTime timestamp;
}
