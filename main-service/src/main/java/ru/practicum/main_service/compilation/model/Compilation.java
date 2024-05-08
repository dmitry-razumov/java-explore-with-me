package ru.practicum.main_service.compilation.model;

import lombok.*;
import ru.practicum.main_service.event.model.Event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Event> events;
    @NotBlank
    private String title;
    private Boolean pinned;
}
