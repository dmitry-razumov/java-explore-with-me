package ru.practicum.main_service.comment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.main_service.utils.Constants.DATE_TIME_FORMAT;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String text;
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime created;
    @Builder.Default
    private Boolean changed = false;
    @ManyToOne(fetch = FetchType.LAZY)
    private User commentator;
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", created=" + created +
                ", changed=" + changed +
                ", commentatorId=" + commentator.getId() +
                ", eventId=" + event.getId() +
                '}';
    }
}
