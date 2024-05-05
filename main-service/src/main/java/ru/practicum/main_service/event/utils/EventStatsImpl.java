package ru.practicum.main_service.event.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.stats_service.EndpointHitDto;
import ru.practicum.stats_service.StatsClient;
import ru.practicum.stats_service.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@ComponentScan("ru.practicum.stats_service")
public class EventStatsImpl implements EventStats {
    private final StatsClient statsClient;
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<Long, Long> getStats(List<Event> events) {
        if (events.isEmpty()) {
            return Collections.emptyMap();
        }
        LocalDateTime start = events.stream()
                .map(Event::getCreatedOn)
                .min(LocalDateTime::compareTo)
                .orElseGet(LocalDateTime::now);
        LocalDateTime end = LocalDateTime.now();

        Map<Long, String> uris = events.stream()
                .collect(Collectors.toMap(
                        Event::getId,
                        event -> String.format("/events/%d", event.getId())));

        ResponseEntity<Object> response = statsClient.getStats(
                start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                new ArrayList<>(uris.values()), true);

        Map<Long, Long> resultMap = new HashMap<>();
        if (response.hasBody() && response.getStatusCode().is2xxSuccessful()) {
            List<ViewStatsDto> viewStatsDtos = new ArrayList<>(Arrays.asList(
                    mapper.convertValue(response.getBody(), new TypeReference<>() {})));
            if (!viewStatsDtos.isEmpty()) {
                Map<String, Long> views = viewStatsDtos.stream()
                        .collect(Collectors.toMap(
                                ViewStatsDto::getUri,
                                ViewStatsDto::getHits
                        ));
                uris.forEach((id, uri) -> resultMap.put(id, views.getOrDefault(uri, 0L)));
            }
        }
        return resultMap;
    }

    @Override
    public void saveStats(HttpServletRequest request) {
        statsClient.createHit(new EndpointHitDto(0, "main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now()));
    }
}
