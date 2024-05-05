package ru.practicum.stats_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats_service.ViewStatsDto;
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
        log.info("создан endpointHit - {}", newEndpointHit);
        return newEndpointHit;
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("запрос start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        List<ViewStatsDto> viewStatsList;
        if (unique) {
            viewStatsList = statsRepository.getViewStatsUnique(start, end, uris);
            log.info("получен unique viewStatsList = {}", viewStatsList);
        } else {
            viewStatsList = statsRepository.getViewStats(start, end, uris);
            log.info("получен viewStatsList = {}", viewStatsList);
        }
        return viewStatsList;
    }
}
