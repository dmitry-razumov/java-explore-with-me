package ru.practicum.main_service.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "commentatorId", source = "commentator.id")
    @Mapping(target = "eventId", source = "event.id")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDto(List<Comment> comments);
}
