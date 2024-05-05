package ru.practicum.main_service.event.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.service.admin.AdminEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final AdminEventService adminEventService;
    private final EventMapper eventMapper;

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody @Valid UpdateEventAdminRequestDto event) {
        log.info("PATCH /admin/events/{} изменениe информации о событии админом {}", eventId, event);
        return eventMapper.toEventFullDto(adminEventService.updateEventByAdmin(eventMapper.toEvent(event), eventId));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsByAdmin(@RequestParam (name = "users", required = false) List<Long> userIds,
                                               @RequestParam (name = "states", required = false) List<String> eventStates,
                                               @RequestParam (name = "categories", required = false) List<Long> categoriesIds,
                                               @RequestParam (required = false)
                                                   @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                               @RequestParam (required = false)
                                                   @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                               @RequestParam (defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam (defaultValue = "10") @Positive Integer size) {

        log.info("GET /admin/events поиск событий для userIds={} eventStates={} categoriesIds={} " +
                        "rangeStart={} rangeEnd={} from={} size={}", userIds, eventStates, categoriesIds,
                        rangeStart, rangeEnd, from, size);
        return eventMapper.toEventFullDto(adminEventService.getEventsByAdmin(userIds, eventStates, categoriesIds,
                rangeStart, rangeEnd, from, size));
    }
}
