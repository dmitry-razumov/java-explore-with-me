package ru.practicum.main_service.compilation.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.model.Compilation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    CompilationDto toDto(Compilation compilation);

    List<CompilationDto> toDto(List<Compilation> compilations);
}
