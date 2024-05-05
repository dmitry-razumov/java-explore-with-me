package ru.practicum.main_service.event.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.service.pub.PublicEventService;
import ru.practicum.main_service.event.utils.EventStats;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final PublicEventService publicEventService;
    private final EventMapper eventMapper;
    private final EventStats eventStats;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@RequestParam (name = "text", required = false) String text,
                                         @RequestParam (name = "categories", required = false) List<Long> categoriesIds,
                                         @RequestParam (required = false) Boolean paid,
                                         @RequestParam (required = false)
                                             @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                         @RequestParam (required = false)
                                             @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                         @RequestParam (required = false) Boolean onlyAvailable,
                                         @RequestParam (required = false) String sort,
                                         @RequestParam (defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam (defaultValue = "10") @Positive  Integer size,
                                         HttpServletRequest request) {

        log.info("GET /events Получение событий с возможностью фильтрации text={} categoriesIds={} paid={} " +
                        "rangeStart={} rangeEnd={} onlyAvailable={} sort={} from={} size={}", text, categoriesIds,
                paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        eventStats.saveStats(request);
        return eventMapper.toEventShortDto(publicEventService.getEvents(text, categoriesIds,
                paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        log.info("GET /events/{} Получение подробной информации об опубликованном событии", id);
        eventStats.saveStats(request);
        return eventMapper.toEventFullDto(publicEventService.getEvent(id));
    }
}
