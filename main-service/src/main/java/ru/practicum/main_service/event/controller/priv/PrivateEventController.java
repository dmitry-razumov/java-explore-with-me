package ru.practicum.main_service.event.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.service.priv.PrivateEventService;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("GET /users/{}/events request events added by current user from={} size={}",
                userId, from, size);
        return privateEventService.getEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @RequestBody @Valid NewEventDto newEventDto) {
        log.info("POST /users/{}/events request for add new event {}", userId, newEventDto);
        return privateEventService.addEvent(newEventDto, userId);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET /users/{}/events/{} request full info about event added current user",
                userId, eventId);
        return privateEventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequestDto event) {
        log.info("PATCH /users/{}/events/{} request for update an event added current user{}",
                userId, eventId, event);
        return privateEventService.updateEvent(userId, eventId, event);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult changeRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest requests) {
        log.info("PATCH /users/{}/events/{}/request for update status of requests for user event {}",
                userId, eventId, requests);
        return privateEventService.changeRequestStatus(userId, eventId, requests);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getEventParticipants(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        log.info("GET /users/{}/events/{}/requests request info about requests for user event",
                userId, eventId);
        return privateEventService.getEventParticipants(userId, eventId);
    }
}
