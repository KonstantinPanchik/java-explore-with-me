package ru.practicum.mainservice.compilation.service.imp;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.CompilationMapper;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.mainservice.compilation.service.CompilationService;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.views.ViewsMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class CompilationServiceImp implements CompilationService {

    private CompilationRepository compilationRepository;
    private EventRepository eventRepository;
    private ViewsMapper viewsMapper;

    @Autowired
    public CompilationServiceImp(CompilationRepository compilationRepository, EventRepository eventRepository, ViewsMapper viewsMapper) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.viewsMapper = viewsMapper;
    }

    @Override
    public List<CompilationDto> getAllCompilations(int from, int size, Boolean pinned) {

        List<Boolean> pinneds = pinned == null ? List.of(true, false) : List.of(pinned);

        Pageable pageable = PageRequest.of(from / size, size);

        List<Compilation> compilations = compilationRepository.findAllByPinnedIn(pinneds, pageable);

        return compilations
                .stream()
                .map(compilation -> CompilationMapper.toDto(compilation, viewsMapper))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = getCompilationByIdOrThrow(compId);
        return CompilationMapper.toDto(compilation, viewsMapper);
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {

        Compilation compilation = CompilationMapper.fromDto(compilationDto);

        return saveEvents(compilationDto, compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = getCompilationByIdOrThrow(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, NewCompilationDto compilationDto) {

        Compilation compilation = getCompilationByIdOrThrow(compId);

        CompilationMapper.updateFromDto(compilation, compilationDto);

        return saveEvents(compilationDto, compilation);
    }

    private CompilationDto saveEvents(NewCompilationDto compilationDto, Compilation compilation) {

        if (compilationDto.getEvents() != null) {

            List<Event> events = eventRepository.findAllById(compilationDto.getEvents());

            Set<Event> eventsForCompilations = events.stream().collect(Collectors.toSet());

            compilation.setEvents(eventsForCompilations);
        }

        compilation = compilationRepository.save(compilation);

        return CompilationMapper.toDto(compilation, viewsMapper);
    }

    private Compilation getCompilationByIdOrThrow(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
    }
}
