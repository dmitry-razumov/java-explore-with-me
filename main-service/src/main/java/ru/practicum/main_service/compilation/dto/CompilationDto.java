package ru.practicum.main_service.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.main_service.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class CompilationDto {
    private List<EventShortDto> events;
    @NotNull
    private Long id;
    @NotNull
    private Boolean pinned;
    @NotBlank
    private String title;
}
