package ru.practicum.main_service.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.comment.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByEventIdAndCommentatorId(Long eventId, Long userId);

    Optional<List<Comment>> findAllByCommentatorId(Long userId);

    Optional<Comment> findByIdAndCommentatorId(Long commentId, Long userId);

    List<Comment> findAllByEventId(Long eventId, Pageable page);
}
