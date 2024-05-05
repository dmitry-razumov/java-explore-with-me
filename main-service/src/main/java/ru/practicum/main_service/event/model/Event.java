package ru.practicum.main_service.event.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @Builder.Default
    private Integer confirmedRequests = 0;
    private LocalDateTime createdOn;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;
    @Embedded
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
    @Transient
    @Builder.Default
    private Long views = 0L;
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;
}
