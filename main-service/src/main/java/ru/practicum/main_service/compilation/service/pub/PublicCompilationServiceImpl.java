package ru.practicum.main_service.compilation.service.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.compilation.repository.CompilationRepository;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.utils.PageRequestCustom;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public List<Compilation> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable page = PageRequestCustom.get(from, size);
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(page).getContent();
        } else {
            compilations = compilationRepository.findAllByPinnedIs(pinned, page);
        }
        for (Compilation compilation : compilations) {
            if (compilation.getEvents() != null) {
                compilation.setEvents(new HashSet<>(compilation.getEvents()));
            }
        }
        log.info("получены подборки событий {}", compilations);
        return compilations;
    }

    @Override
    public Compilation getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
        if (compilation.getEvents() != null) {
            compilation.setEvents(new HashSet<>(compilation.getEvents()));
        }
        log.info("получена подборка событий {}", compilation);
        return compilation;
    }
}
