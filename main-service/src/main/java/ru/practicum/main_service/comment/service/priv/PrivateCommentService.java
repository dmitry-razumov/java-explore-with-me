package ru.practicum.main_service.comment.service.priv;

import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.dto.NewCommentDto;

import java.util.List;

public interface PrivateCommentService {
    CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    List<CommentDto> getUserComments(Long userId, Long eventId);

    CommentDto updateComment(Long userId, Long commentId, NewCommentDto updateCommentDto);

    void deleteComment(Long userId, Long commentId);
}
