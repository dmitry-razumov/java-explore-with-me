package ru.practicum.main_service.compilation.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.compilation.repository.CompilationRepository;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Compilation saveCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
        }
        Compilation compilation = compilationRepository.save(
                Compilation.builder()
                        .title(newCompilationDto.getTitle())
                        .pinned(newCompilationDto.getPinned())
                        .events(new HashSet<>(events))
                        .build()
        );
        log.info("создана подборка {}", compilation);
        return compilation;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
        compilationRepository.deleteById(compId);
        log.info("удалена подборка с id={}", compId);
    }

    @Override
    @Transactional
    public Compilation updateCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(new HashSet<>(eventRepository.findAllById(updateCompilationRequest.getEvents())));
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        log.info("обновлена подборка {}", compilation);
        return compilation;
    }
}
