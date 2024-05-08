package ru.practicum.main_service.event.service.pub;

import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getEvents(String text, List<Long> categoriesIds, Boolean paid,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                  String sort, Integer from, Integer size);

    EventFullDto getEvent(Long id);
}
