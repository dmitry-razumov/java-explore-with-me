package ru.practicum.stats_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats_service.EndpointHitDto;
import ru.practicum.stats_service.ViewStatsDto;
import ru.practicum.stats_service.mapper.StatsServerMapper;
import ru.practicum.stats_service.service.StatsService;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@ComponentScan(basePackageClasses = StatsServerMapper.class)
public class StatsController {
    private final StatsService statsService;
    private final StatsServerMapper statsServerMapper;
    private static final String HIT_ENDPOINT = "/hit";
    private static final String STATS_ENDPOINT = "/stats";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @GetMapping(STATS_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
                                       @RequestParam (required = false) List<String> uris,
                                       @RequestParam (required = false, defaultValue = "false") Boolean unique) {
        log.info("GET {}?start={}&end={}&uris={}&unique={}", STATS_ENDPOINT, start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping(HIT_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("POST with body endpointHitDto = {}", endpointHitDto);
        return statsServerMapper.toHitDto(statsService.createHit(statsServerMapper.toHit(endpointHitDto)));
    }
}
