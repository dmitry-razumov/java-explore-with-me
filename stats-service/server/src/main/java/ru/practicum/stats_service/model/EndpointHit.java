package ru.practicum.stats_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "endpoint_hits")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Идентификатор сервиса должен быть задан")
    private String app;
    @NotBlank(message = "URI для которого был осуществлен запрос должен быть задан")
    private String uri;
    @NotBlank(message = "IP-адрес пользователя, осуществившего запрос, не должен быть пустым")
    private String ip;
    @NotNull(message = "Дата и время, когда был совершен запрос к эндпоинту, не должно быть null")
    private LocalDateTime hitTimestamp;
}
