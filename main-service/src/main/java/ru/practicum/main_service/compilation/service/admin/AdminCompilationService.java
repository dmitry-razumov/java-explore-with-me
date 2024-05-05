package ru.practicum.main_service.compilation.service.admin;

import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main_service.compilation.model.Compilation;

public interface AdminCompilationService {
    Compilation saveCompilation(NewCompilationDto newCompilation);

    void deleteCompilation(Long compId);

    Compilation updateCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId);
}
