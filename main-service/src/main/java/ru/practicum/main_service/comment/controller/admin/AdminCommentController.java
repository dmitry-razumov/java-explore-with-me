package ru.practicum.main_service.comment.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.comment.service.admin.AdminCommentService;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/comments/{commentId}")
public class AdminCommentController {
    private final AdminCommentService commentService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        log.info("DELETE /comments/{} delete comment by admin", commentId);
        commentService.deleteComment(commentId);
    }
}
