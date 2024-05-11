package ru.practicum.main_service.request.controller.priv;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("POST /users/{}/requests?eventId={} request for add request for participation in an event by user",
                userId, eventId);
        return requestService.addParticipationRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        log.info("GET /users/{}/requests request info about user requests in other people's event",
                userId);
        return requestService.getUserRequests(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("PATCH /users/{}/requests/{}/cancel request for cancel user request to participate in an event",
                userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }
}
