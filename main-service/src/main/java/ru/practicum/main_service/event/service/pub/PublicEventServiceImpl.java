package ru.practicum.main_service.event.service.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.enums.EventSort;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.event.utils.EventStats;
import ru.practicum.main_service.exception.BadRequestException;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.utils.PageRequestCustom;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final EventStats eventStats;
    private final EventMapper eventMapper;

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categoriesIds, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                         String sort, Integer from, Integer size) {
        if (rangeStart != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("the end date and time must be later than the start date and time");
        }
        Specification<Event> specification = (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (text != null) {
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                        builder.like(builder.lower(root.get("description")), "%" + text.toLowerCase() + "%")
                ));
            }
            if (categoriesIds != null && !categoriesIds.isEmpty()) {
                predicates.add(root.join("category", JoinType.INNER).get("id").in(categoriesIds));
            }
            if (paid != null) {
                predicates.add(builder.equal(root.get("paid"), paid));
            }
            predicates.add(builder.equal(root.get("state"), EventState.PUBLISHED));
            predicates.add(builder.greaterThanOrEqualTo(root.get("eventDate"),
                    rangeStart != null ? rangeStart : LocalDateTime.now()));
            if (rangeEnd != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
            }
            if (onlyAvailable != null && onlyAvailable) {
                predicates.add(builder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests")));
            }
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Pageable page = PageRequestCustom.get(from, size);
        List<Event> events = eventRepository.findAll(specification, page).getContent();
        Map<Long, Long> views = eventStats.getStats(events);
        log.info("==== views ==== {}", views);
        events.forEach(event -> event.setViews(views.getOrDefault(event.getId(), 0L)));
        if (sort != null) {
            EventSort eventSort = getSort(sort);
            List<Event> sorted = new ArrayList<>();
            if (eventSort != null) {
                switch (eventSort) {
                    case EVENT_DATE:
                        sorted = events.stream().sorted(Comparator.comparing(Event::getEventDate).reversed())
                                .collect(Collectors.toList());
                        break;
                    case VIEWS:
                        sorted = events.stream().sorted(Comparator.comparing(Event::getViews).reversed())
                                .collect(Collectors.toList());
                        break;
                    default:
                        break;
                }
            }
            log.info("find sorted events {}", sorted);
            return eventMapper.toEventShortDto(sorted);
        }
        log.info("find events without sort {}", events);
        return eventMapper.toEventShortDto(events);
    }

    @Override
    public EventFullDto getEvent(Long id) {
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", id)));
        Map<Long, Long> views = eventStats.getStats(List.of(event));
        event.setViews(views.getOrDefault(event.getId(), 0L));
        log.info("find an event {}", event);
        return eventMapper.toEventFullDto(event);
    }

    private EventSort getSort(String sort) {
        try {
            return EventSort.valueOf(sort);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
