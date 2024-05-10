package ru.practicum.main_service.event.mapper;

import org.mapstruct.*;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.enums.StateAction;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.exception.BadRequestException;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "category.id", source = "category")
    Event toEvent(NewEventDto newEventDto);

    default boolean mapAnnotation(String annotation) {
        if (annotation != null && annotation.isBlank()) {
                throw new BadRequestException("Field: annotation. Error: must not be blank");
            }
        return true;
    }

    default boolean mapDescription(String description) {
        if (description != null && description.isBlank()) {
            throw new BadRequestException("Field: description. Error: must not be blank");
        }
        return true;
    }

    default boolean mapTitle(String title) {
        if (title != null && title.isBlank()) {
            throw new BadRequestException("Field: title. Error: must not be blank");
        }
        return true;
    }

    @Mapping(target = "category.id", source = "category")
    @Mapping(target = "state", source = "stateAction")
    @Mapping(target = "annotation",
            conditionExpression = "java(mapAnnotation(updateEventAdminRequestDto.getAnnotation()))",
            source = "annotation")
    @Mapping(target = "description",
            conditionExpression = "java(mapDescription(updateEventAdminRequestDto.getDescription()))",
            source = "description")
    @Mapping(target = "title",
            conditionExpression = "java(mapTitle(updateEventAdminRequestDto.getTitle()))",
            source = "title")
    Event toEvent(UpdateEventAdminRequestDto updateEventAdminRequestDto);

    @Mapping(target = "category.id", source = "category")
    @Mapping(target = "state", source = "stateAction")
    @Mapping(target = "annotation",
            conditionExpression = "java(mapAnnotation(updateEventUserRequestDto.getAnnotation()))",
            source = "annotation")
    @Mapping(target = "description",
            conditionExpression = "java(mapDescription(updateEventUserRequestDto.getDescription()))",
            source = "description")
    @Mapping(target = "title",
            conditionExpression = "java(mapTitle(updateEventUserRequestDto.getTitle()))",
            source = "title")
    Event toEvent(UpdateEventUserRequestDto updateEventUserRequestDto);

    @ValueMappings({
            @ValueMapping(target = "PUBLISHED", source = "PUBLISH_EVENT"),
            @ValueMapping(target = "CANCELED", source = "REJECT_EVENT"),
            @ValueMapping(target = "PENDING", source = "SEND_TO_REVIEW"),
            @ValueMapping(target = "CANCELED", source = "CANCEL_REVIEW"),
            @ValueMapping(target = MappingConstants.NULL, source = MappingConstants.ANY_UNMAPPED)
    })
    EventState stateActionToEventState(StateAction stateAction);

    EventFullDto toEventFullDto(Event event);

    List<EventFullDto> toEventFullDto(List<Event> events);

    List<EventShortDto> toEventShortDto(List<Event> events);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEvent(Event event, @MappingTarget Event updateEvent);
}
