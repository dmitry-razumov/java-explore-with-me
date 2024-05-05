package ru.practicum.stats_service.service;

import ru.practicum.stats_service.ViewStatsDto;
import ru.practicum.stats_service.model.EndpointHit;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHit createHit(EndpointHit toHitEntity);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
