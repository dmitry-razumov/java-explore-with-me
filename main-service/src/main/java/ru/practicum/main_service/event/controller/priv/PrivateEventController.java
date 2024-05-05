package ru.practicum.main_service.event.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.service.priv.PrivateEventService;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.mapper.RequestMapper;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final PrivateEventService privateEventService;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("GET /users/{}/events Получение событий, добавленных текущим пользователем from={} size={}",
                userId, from, size);
        return eventMapper.toEventShortDto(privateEventService.getEvents(userId, from, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @RequestBody @Valid NewEventDto newEventDto) {
        log.info("POST /users/{}/events Добавление нового события {}", userId, newEventDto);
        return eventMapper.toEventFullDto(privateEventService.addEvent(eventMapper.toEvent(newEventDto), userId));
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET /users/{}/events/{} Получение полной информации о событии добавленном текущим пользователем",
                userId, eventId);
        return eventMapper.toEventFullDto(privateEventService.getEvent(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequestDto event) {
        log.info("GET /users/{}/events/{} Изменение события добавленного текущим пользователем {}",
                userId, eventId, event);
        return eventMapper.toEventFullDto(privateEventService.updateEvent(userId, eventId,
                eventMapper.toEvent(event)));
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult changeRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest requests) {
        log.info("PATCH /users/{}/events/{}/requests Изменение статуса заявок на участие в событии пользователя {}",
                userId, eventId, requests);
        return privateEventService.changeRequestStatus(userId, eventId, requests);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getEventParticipants(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        log.info("GET /users/{}/events/{}/requests Получение о запросах на участие в событии текущего пользователя",
                userId, eventId);
        return requestMapper.toDto(privateEventService.getEventParticipants(userId, eventId));
    }
}
