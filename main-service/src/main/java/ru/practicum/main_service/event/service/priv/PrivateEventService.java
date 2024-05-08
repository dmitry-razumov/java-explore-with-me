package ru.practicum.main_service.event.service.priv;

import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {
    EventFullDto addEvent(NewEventDto newEventDto, Long userId);

    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest requests);

    List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequestDto event);
}
