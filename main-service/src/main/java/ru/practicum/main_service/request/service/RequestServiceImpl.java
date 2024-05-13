package ru.practicum.main_service.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exception.ConditionNotMetException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.enums.RequestStatus;
import ru.practicum.main_service.request.mapper.RequestMapper;
import ru.practicum.main_service.request.model.ParticipationRequest;
import ru.practicum.main_service.request.repository.RequestRepository;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConditionNotMetException("Can't add the same request");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConditionNotMetException("Cannot participate in an unpublished event");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConditionNotMetException("The event initiator cannot add a request to participate in his event");
        }
        if (event.getParticipantLimit() != 0) {
            Integer requests = requestRepository.countAllByEventIdAndStatusIs(eventId, RequestStatus.CONFIRMED);
            if (event.getParticipantLimit().equals(requests)) {
                throw new ConditionNotMetException("The event has reached the limit of participation requests");
            }
        }
        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(!event.getRequestModeration() || event.getParticipantLimit() == 0
                        ? RequestStatus.CONFIRMED : RequestStatus.PENDING)
                .build();
        ParticipationRequest newRequest = requestRepository.save(request);
        log.info("request was save {}", newRequest);
        return requestMapper.toDto(newRequest);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(userId);
        log.info("find requests {}", requests);
        return requestMapper.toDto(requests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d was not found", requestId)));
        request.setStatus(RequestStatus.CANCELED);
        log.info("request was cancel {}", request);
        return requestMapper.toDto(request);
    }
}
