package ru.practicum.stats_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats_service.ViewStatsDto;
import ru.practicum.stats_service.model.EndpointHit;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.stats_service.ViewStatsDto(eph.app, eph.uri, count(distinct(eph.ip))) " +
            "from EndpointHit as eph " +
            "where eph.hitTimestamp between :start and :end " +
            "and ((:uris) is null or eph.uri in (:uris)) " +
            "group by eph.app, eph.uri " +
            "order by count(eph.uri) desc")
    List<ViewStatsDto> getViewStatsUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                          @Param("uris") List<String> uris);

    @Query("select new ru.practicum.stats_service.ViewStatsDto(eph.app, eph.uri, count(eph.uri)) " +
            "from EndpointHit as eph " +
            "where eph.hitTimestamp between :start and :end " +
            "and ((:uris) is null or eph.uri in (:uris)) " +
            "group by eph.app, eph.uri " +
            "order by count(eph.uri) desc")
    List<ViewStatsDto> getViewStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                    @Param("uris") List<String> uris);
}
