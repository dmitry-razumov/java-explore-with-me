package ru.practicum.main_service.event.utils;

import ru.practicum.main_service.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface EventStats {
    Map<Long, Long> getStats(List<Event> events);

    void saveStats(HttpServletRequest request);
}
