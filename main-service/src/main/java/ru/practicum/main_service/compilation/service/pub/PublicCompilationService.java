package ru.practicum.main_service.compilation.service.pub;

import ru.practicum.main_service.compilation.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long compId);
}
