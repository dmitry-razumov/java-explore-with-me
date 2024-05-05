package ru.practicum.main_service.event.service.priv;

import ru.practicum.main_service.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.request.model.ParticipationRequest;

import java.util.List;

public interface PrivateEventService {
    Event addEvent(Event event, Long userId);

    List<Event> getEvents(Long userId, Integer from, Integer size);

    EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest requests);

    List<ParticipationRequest> getEventParticipants(Long userId, Long eventId);

    Event getEvent(Long userId, Long eventId);

    Event updateEvent(Long userId, Long eventId, Event event);
}
