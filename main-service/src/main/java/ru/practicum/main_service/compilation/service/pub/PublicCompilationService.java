package ru.practicum.main_service.compilation.service.pub;

import ru.practicum.main_service.compilation.model.Compilation;

import java.util.List;

public interface PublicCompilationService {
    List<Compilation> getCompilations(Boolean pinned, Integer from, Integer size);

    Compilation getCompilation(Long compId);
}
