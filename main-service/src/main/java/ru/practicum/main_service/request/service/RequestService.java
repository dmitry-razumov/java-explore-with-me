package ru.practicum.main_service.request.service;

import ru.practicum.main_service.request.model.ParticipationRequest;

import java.util.List;

public interface RequestService {
    ParticipationRequest addParticipationRequest(Long userId, Long eventId);

    List<ParticipationRequest> getUserRequests(Long userId);

    ParticipationRequest cancelRequest(Long userId, Long requestId);
}
