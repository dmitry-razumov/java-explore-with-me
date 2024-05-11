package ru.practicum.stats_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats_service.ViewStatsDto;
import ru.practicum.stats_service.exception.ValidationException;
import ru.practicum.stats_service.repository.StatsRepository;
import ru.practicum.stats_service.model.EndpointHit;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public EndpointHit createHit(EndpointHit endpointHit) {
        EndpointHit newEndpointHit = statsRepository.save(endpointHit);
        log.info("create endpointHit - {}", newEndpointHit);
        return newEndpointHit;
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("request start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        if (end.isBefore(start)) {
            throw new ValidationException("the start date cannot be later than the end date of selection");
        }
        List<ViewStatsDto> viewStatsList;
        if (unique) {
            viewStatsList = statsRepository.getViewStatsUnique(start, end, uris);
            log.info("get unique viewStatsList = {}", viewStatsList);
        } else {
            viewStatsList = statsRepository.getViewStats(start, end, uris);
            log.info("get viewStatsList = {}", viewStatsList);
        }
        return viewStatsList;
    }
}
