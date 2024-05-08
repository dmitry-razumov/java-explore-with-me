package ru.practicum.main_service.compilation.service.admin;

import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto saveCompilation(NewCompilationDto newCompilation);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId);
}
