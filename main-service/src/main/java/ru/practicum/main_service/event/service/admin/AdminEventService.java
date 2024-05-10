package ru.practicum.main_service.event.service.admin;

import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.UpdateEventAdminRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    EventFullDto updateEventByAdmin(UpdateEventAdminRequestDto updateEventAdminRequestDto, Long eventId);

    List<EventFullDto> getEventsByAdmin(List<Long> userIds, List<String> eventStates, List<Long> categoriesIds,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
}
