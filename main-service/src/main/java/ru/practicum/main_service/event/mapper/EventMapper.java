package ru.practicum.main_service.event.mapper;

import org.mapstruct.*;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.enums.StateAction;
import ru.practicum.main_service.event.model.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "category.id", source = "category")
    Event toEvent(NewEventDto newEventDto);

    @Mapping(target = "category.id", source = "category")
    @Mapping(target = "state", source = "stateAction")
    Event toEvent(UpdateEventAdminRequestDto updateEventAdminRequestDto);

    @Mapping(target = "category.id", source = "category")
    @Mapping(target = "state", source = "stateAction")
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
