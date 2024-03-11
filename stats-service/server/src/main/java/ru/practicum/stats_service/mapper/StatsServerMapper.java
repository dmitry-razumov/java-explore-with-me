package ru.practicum.stats_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stats_service.EndpointHitDto;
import ru.practicum.stats_service.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface StatsServerMapper {
    @Mapping(target = "hitTimestamp", source = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EndpointHit toHit(EndpointHitDto endpointHitDto);

    EndpointHitDto toHitDto(EndpointHit endpointHit);
}
