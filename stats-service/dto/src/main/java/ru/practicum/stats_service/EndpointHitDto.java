package ru.practicum.stats_service;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class EndpointHitDto {
    private long id;
    @NotBlank(message = "Идентификатор сервиса должен быть задан")
    private String app;
    @NotBlank(message = "URI для которого был осуществлен запрос должен быть задан")
    private String uri;
    @NotBlank(message = "IP-адрес пользователя, осуществившего запрос, не должен быть пустым")
    private String ip;
    @NotBlank(message = "Дата и время, когда был совершен запрос к эндпоинту, не должно быть пустым")
    private String timestamp;
}
