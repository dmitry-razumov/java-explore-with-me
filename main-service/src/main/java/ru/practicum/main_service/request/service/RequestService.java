package ru.practicum.main_service.request.service;

import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto addParticipationRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
