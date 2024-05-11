package ru.practicum.main_service.comment.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.service.pub.PublicCommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
public class PublicCommentController {
    private final PublicCommentService commentService;

    @GetMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getComment(@PathVariable Long id) {
        log.info("GET /comments/{} request comment", id);
        return commentService.getComment(id);
    }

    @GetMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getEventComments(@PathVariable Long eventId,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                             @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET /events/{}/comments request comments for event from={} size={}", eventId, from, size);
        return commentService.getEventComments(eventId, from, size);
    }
}
