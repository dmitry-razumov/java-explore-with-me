package ru.practicum.main_service.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static ru.practicum.main_service.utils.Constants.DATE_TIME_FORMAT;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime created;
    private Boolean changed;
    private Long commentatorId;
    private Long eventId;
}
