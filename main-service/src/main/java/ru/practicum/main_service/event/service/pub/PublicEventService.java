package ru.practicum.main_service.event.service.pub;

import ru.practicum.main_service.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    List<Event> getEvents(String text, List<Long> categoriesIds, Boolean paid,
                          LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                          String sort, Integer from, Integer size);

    Event getEvent(Long id);
}
