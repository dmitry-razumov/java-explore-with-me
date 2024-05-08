package ru.practicum.main_service.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.request.enums.RequestStatus;
import ru.practicum.main_service.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.main_service.utils.Constants.DATE_TIME_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participation_request")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime created;
    @ManyToOne
    private Event event;
    @ManyToOne
    private User requester;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
