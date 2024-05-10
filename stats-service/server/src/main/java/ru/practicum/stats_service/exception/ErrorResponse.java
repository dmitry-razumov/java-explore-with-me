package ru.practicum.stats_service.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
}
