package ru.practicum.main_service.comment.service.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.mapper.CommentMapper;
import ru.practicum.main_service.comment.model.Comment;
import ru.practicum.main_service.comment.repository.CommentRepository;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.utils.PageRequestCustom;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PublicCommentServiceImpl implements PublicCommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto getComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d was not found", id)));
        log.info("Получен комментарий {}", comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public List<CommentDto> getEventComments(Long eventId, Integer from, Integer size) {
        Pageable page = PageRequestCustom.get(from, size);
        List<Comment> comments = commentRepository.findAllByEventId(eventId, page);
        log.info("Получены комментарии {} from={} size={}", comments, from, size);
        return commentMapper.toDto(comments);
    }
}
