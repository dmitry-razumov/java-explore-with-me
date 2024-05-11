package ru.practicum.main_service.comment.service.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.dto.NewCommentDto;
import ru.practicum.main_service.comment.mapper.CommentMapper;
import ru.practicum.main_service.comment.model.Comment;
import ru.practicum.main_service.comment.repository.CommentRepository;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exception.ConditionNotMetException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConditionNotMetException("Нельзя добавить комментарий к неопубликованному событию");
        }
        Comment comment = Comment.builder()
                .created(LocalDateTime.now())
                .text(newCommentDto.getText())
                .changed(false)
                .commentator(user)
                .event(event)
                .build();
        Comment newComment = commentRepository.save(comment);
        log.info("Создан комментарий {}", newComment);
        return commentMapper.toDto(newComment);
    }

    @Override
    public List<CommentDto> getUserComments(Long userId, Long eventId) {
        List<Comment> comments;
        if (eventId == null) {
            comments = commentRepository.findAllByCommentatorId(userId)
                    .orElse(Collections.emptyList());
            log.info("получены все комментарии {} для userId={}", comments, userId);
        } else {
            comments = commentRepository.findByEventIdAndCommentatorId(eventId, userId)
                    .orElse(Collections.emptyList());
            log.info("получены комментарии {} для eventId={} userId={}", comments, eventId, userId);
        }
        return commentMapper.toDto(comments);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto updateCommentDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        Comment comment = commentRepository.findByIdAndCommentatorId(commentId, userId)
                .orElseThrow(() -> new ConditionNotMetException(String.format("Нельзя изменить чужой комментарий id=%d",
                        commentId)));
        comment.setText(updateCommentDto.getText());
        comment.setChanged(true);
        log.info("Изменён комментарий {}", comment);
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        Comment comment = commentRepository.findByIdAndCommentatorId(commentId, userId)
                .orElseThrow(() -> new ConditionNotMetException(String.format("Нельзя удалить чужой комментарий id=%d",
                        commentId)));
        commentRepository.delete(comment);
        log.info("Удалён комментарий {}", comment);
    }
}
