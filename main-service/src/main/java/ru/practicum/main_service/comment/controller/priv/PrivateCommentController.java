package ru.practicum.main_service.comment.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.dto.NewCommentDto;
import ru.practicum.main_service.comment.service.priv.PrivateCommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final PrivateCommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Long userId, @RequestParam Long eventId,
                                 @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("POST /users/{}/comments?eventId={} add comment {} from userId for event with EventId",
                userId, eventId, newCommentDto);
        return commentService.addComment(userId, eventId, newCommentDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getUserComments(@PathVariable Long userId, @RequestParam(required = false) Long eventId) {
        log.info("GET /users/{}/comments?eventId={} request comments from userId for event with EventId",
                userId, eventId);
        return commentService.getUserComments(userId, eventId);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Long userId, @PathVariable Long commentId,
                                    @RequestBody @Valid NewCommentDto updateCommentDto) {
        log.info("PATCH /users/{}/comments/{} Изменение комментария пользователем, новый комментарий {}",
                userId, commentId, updateCommentDto);
        return commentService.updateComment(userId, commentId, updateCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserComment(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("DELETE /users/{}/comments/{} Удаление комментария пользователем",
                userId, commentId);
        commentService.deleteComment(userId, commentId);
    }
}
