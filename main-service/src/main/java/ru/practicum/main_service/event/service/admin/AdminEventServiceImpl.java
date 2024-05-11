package ru.practicum.main_service.event.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.repository.CategoryRepository;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.event.utils.EventStats;
import ru.practicum.main_service.exception.BadRequestException;
import ru.practicum.main_service.exception.ConditionNotMetException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.utils.PageRequestCustom;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService {
    private static final int ADMIN_DELTA_IN_HOURS = 1;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final EventStats eventStats;

    // admin
    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequestDto updateEventAdminRequestDto, Long eventId) {
        Event event = eventMapper.toEvent(updateEventAdminRequestDto);
        Event updateEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        log.info("event for update {}", updateEvent);
        LocalDateTime publishDate = updateEvent.getPublishedOn() != null ? updateEvent.getPublishedOn()
                : LocalDateTime.now();
        LocalDateTime newEventDate = event.getEventDate();
        log.info("====== publishDate {}", publishDate);
        log.info("== updateEvent.getEventDate().minusHours(ADMIN_DELTA_IN_HOURS) {} ",
                updateEvent.getEventDate().minusHours(ADMIN_DELTA_IN_HOURS));
        log.info("== updateEvent.getEventDate().minusHours(ADMIN_DELTA_IN_HOURS).isBefore(publishDate) {}",
                updateEvent.getEventDate().minusHours(ADMIN_DELTA_IN_HOURS).isBefore(publishDate));
        if (updateEvent.getEventDate().minusHours(ADMIN_DELTA_IN_HOURS).isBefore(publishDate)
            || (newEventDate != null && newEventDate.minusHours(ADMIN_DELTA_IN_HOURS).isBefore(publishDate))) {
                throw new BadRequestException("Field: eventDate. Error: date must be no earlier than an hour " +
                        "from published date. Value: " + updateEvent.getEventDate());
        }
        if (event.getState() != null &&
                event.getState().equals(EventState.PUBLISHED) && !updateEvent.getState().equals(EventState.PENDING)) {
            throw new ConditionNotMetException("Cannot publish the event because it's not in the right state: " +
                    updateEvent.getState());
        }
        if (event.getState() != null &&
                event.getState().equals(EventState.CANCELED) && updateEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConditionNotMetException("Cannot cancel the event because it's not in the right state: " +
                    updateEvent.getState());
        }
        if (event.getCategory() != null) {
            Long catId = event.getCategory().getId();
            Category category = catId != null
                    ? categoryRepository.findById(catId)
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)))
                    : null;
            event.setCategory(category);
        }
        if (event.getState() != null && event.getState().equals(EventState.PUBLISHED)) {
            event.setPublishedOn(publishDate);
        }
        eventMapper.updateEvent(event, updateEvent);
        Map<Long, Long> views = eventStats.getStats(List.of(updateEvent));
        updateEvent.setViews(views.getOrDefault(updateEvent.getId(), 0L));
        log.info("event was update {}", updateEvent);
        return eventMapper.toEventFullDto(updateEvent);
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> userIds, List<String> eventStates, List<Long> categoriesIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Specification<Event> specification = (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userIds != null && !userIds.isEmpty()) {
                predicates.add(root.get("initiator").in(userIds));
            }
            if (eventStates != null) {
                List<EventState> states = eventStates.stream().map(this::getState)
                    .filter(Objects::nonNull).collect(Collectors.toList());
                predicates.add(root.get("state").in(states));
            }
            if (categoriesIds != null && !categoriesIds.isEmpty()) {
                predicates.add(root.join("category", JoinType.INNER).get("id").in(categoriesIds));
            }
            if (rangeStart != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            }
            if (rangeEnd != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
            }
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Pageable page = PageRequestCustom.get(from, size);
        List<Event> events = eventRepository.findAll(specification, page).getContent();
        Map<Long, Long> views = eventStats.getStats(events);
        events.forEach(event -> event.setViews(views.getOrDefault(event.getId(), 0L)));
        log.info("find events {}", events);
        return eventMapper.toEventFullDto(events);
    }

    private EventState getState(String state) {
        try {
            return EventState.valueOf(state);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
