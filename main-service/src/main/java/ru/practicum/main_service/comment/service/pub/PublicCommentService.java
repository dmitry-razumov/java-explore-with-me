package ru.practicum.main_service.comment.service.pub;

import ru.practicum.main_service.comment.dto.CommentDto;

import java.util.List;

public interface PublicCommentService {
    CommentDto getComment(Long id);

    List<CommentDto> getEventComments(Long eventId, Integer from, Integer size);
}
