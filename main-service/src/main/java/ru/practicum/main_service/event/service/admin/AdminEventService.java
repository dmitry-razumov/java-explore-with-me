package ru.practicum.main_service.event.service.admin;

import ru.practicum.main_service.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    Event updateEventByAdmin(Event event, Long eventId);

    List<Event> getEventsByAdmin(List<Long> userIds, List<String> eventStates, List<Long> categoriesIds,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
}
