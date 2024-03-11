package ru.practicum.stats_service;

import lombok.Value;

@Value
public class ViewStatsDto {
    String app;
    String uri;
    Long hits;
}
