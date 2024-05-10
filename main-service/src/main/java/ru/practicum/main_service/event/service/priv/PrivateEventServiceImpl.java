package ru.practicum.main_service.event.service.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.repository.CategoryRepository;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.event.utils.EventStats;
import ru.practicum.main_service.exception.BadRequestException;
import ru.practicum.main_service.exception.ConditionNotMetException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;
import ru.practicum.main_service.request.enums.RequestStatus;
import ru.practicum.main_service.request.mapper.RequestMapper;
import ru.practicum.main_service.request.model.ParticipationRequest;
import ru.practicum.main_service.request.repository.RequestRepository;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;
import ru.practicum.main_service.utils.PageRequestCustom;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateEventServiceImpl implements PrivateEventService {
    private static final int USER_DELTA_IN_HOURS = 2;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final EventMapper eventMapper;
    private final EventStats eventStats;

    // priv
    @Override
    @Transactional
    public EventFullDto addEvent(NewEventDto newEventDto, Long userId) {
        Event event = eventMapper.toEvent(newEventDto);
        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(USER_DELTA_IN_HOURS))) {
            throw new BadRequestException("Field: eventDate. Error: должно содержать дату, которая еще не наступила." +
                    " Value: " + event.getEventDate());
        }
        Long catId = event.getCategory().getId();
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(initiator);
        event.setState(EventState.PENDING);
        Event newEvent = eventRepository.save(event);
        log.info("создано событие {}", newEvent);
        return eventMapper.toEventFullDto(newEvent);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        Pageable page = PageRequestCustom.get(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, page);
        Map<Long, Long> views = eventStats.getStats(events);
        events.forEach(event -> event.setViews(views.getOrDefault(event.getId(), 0L)));
        log.info("получены события {}", events);
        return eventMapper.toEventShortDto(events);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequests) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d for user with id=%d" +
                        " was not found", eventId, userId)));
        RequestStatus requestStatus = RequestStatus.getFromString(eventRequests.getStatus())
                .orElseThrow(() -> new BadRequestException("Request must have status CONFIRMED|REJECTED"));
        Integer notPendingRequestsByIds = requestRepository.countAllByEventIdAndStatusNotAndIdIn(eventId,
                RequestStatus.PENDING, eventRequests.getRequestIds());
        if (notPendingRequestsByIds != 0) {
            throw new ConditionNotMetException("Cтатус можно изменить только у заявок в состоянии ожидания");
        }
        List<ParticipationRequest> pendingRequests = requestRepository.findAllByEventIdAndStatusAndIdIn(eventId,
                RequestStatus.PENDING, eventRequests.getRequestIds());
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmedList = new ArrayList<>();
        List<ParticipationRequestDto> rejectedList = new ArrayList<>();
        int confirmedRequests = event.getConfirmedRequests();
        switch (requestStatus) {
            case CONFIRMED :
                int confirmedLimit = event.getParticipantLimit();
                log.info("== case CONFIRMED confirmedLimit {}", confirmedLimit);
                if (confirmedLimit == 0) {
                    throw new BadRequestException("Подтверждение заявок не требуется, для события лимит заявок равен 0");
                }
                if (!event.getRequestModeration()) {
                    throw new BadRequestException("Подтверждение заявок не требуется, у события отключена пре-модерация заявок");
                }
                if (confirmedRequests >= confirmedLimit) {
                    throw new ConditionNotMetException("The participant limit has been reached");
                }

                for (ParticipationRequest request: pendingRequests) {
                    if (confirmedRequests < confirmedLimit) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        confirmedList.add(requestMapper.toDto(request));
                        confirmedRequests++;
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                        rejectedList.add(requestMapper.toDto(request));
                    }
                    log.info("== case CONFIRMED request {}, confirmedRequests {}", request, confirmedRequests);
                }
                event.setConfirmedRequests(confirmedRequests);
                break;
            case REJECTED:
                for (ParticipationRequest request: pendingRequests) {
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedList.add(requestMapper.toDto(request));
                    log.info("== case REJECTED request {}", request);
                }
                break;
            default:
                throw new BadRequestException(String.format("Request status must be CONFIRMED|REJECTED, but: %s",
                        eventRequests.getStatus()));
        }
        requestRepository.saveAll(pendingRequests);
        result.setConfirmedRequests(confirmedList);
        result.setRejectedRequests(rejectedList);
        log.info("update requests status {}", result);
        return result;
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId) {
        if (eventRepository.findByIdAndInitiatorId(eventId, userId).isEmpty()) {
            return Collections.emptyList();
        }
        List<ParticipationRequest> requests = requestRepository.findAllByEventId(eventId);
        log.info("получены requests {} для eventId={} userId={}", requests, eventId, userId);
        return requestMapper.toDto(requests);
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d for user with id=%d" +
                        " was not found", eventId, userId)));
        Map<Long, Long> views = eventStats.getStats(List.of(event));
        event.setViews(views.getOrDefault(event.getId(), 0L));
        log.info("Для user id={} получено событие {}", userId, event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequestDto updateEventUserRequestDto) {
        Event event = eventMapper.toEvent(updateEventUserRequestDto);
        Event updateEvent = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d for user with id=%d" +
                        " was not found", eventId, userId)));
        log.info("событие для обновления {}", updateEvent);
        LocalDateTime dateTimeNow = LocalDateTime.now();
        LocalDateTime newEventDate = event.getEventDate();
        if (updateEvent.getEventDate().minusHours(USER_DELTA_IN_HOURS).isBefore(dateTimeNow)
                || (newEventDate != null && newEventDate.minusHours(USER_DELTA_IN_HOURS).isBefore(dateTimeNow))) {
            throw new BadRequestException("Field: eventDate. Error: должно содержать дату, не ранее чем через 2 часа " +
                    "от текущего момента. Value: " + updateEvent.getEventDate());
        }
        if (updateEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConditionNotMetException("Only pending or canceled events can be changed");
        }
        if (event.getCategory() != null) {
            Long catId = event.getCategory().getId();
            Category category = catId != null
                    ? categoryRepository.findById(catId)
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)))
                    : null;
            event.setCategory(category);
        }
        eventMapper.updateEvent(event, updateEvent);
        Map<Long, Long> views = eventStats.getStats(List.of(updateEvent));
        updateEvent.setViews(views.getOrDefault(updateEvent.getId(), 0L));
        log.info("обновлено событие {}", updateEvent);
        return eventMapper.toEventFullDto(updateEvent);
    }
}
