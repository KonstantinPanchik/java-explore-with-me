package ru.practicum.mainservice.compilation.dto;

import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.views.ViewsMapper;

import java.util.HashSet;
import java.util.Set;

public class CompilationMapper {


    public static CompilationDto toDto(Compilation compilation) {

        Set<EventShortDto> eventShortDtoList = new HashSet<>(ViewsMapper
                .toEventShortDtosWithViews(compilation.getEvents()));

        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(eventShortDtoList)
                .build();
    }

    public static Compilation fromDto(NewCompilationDto compilationDto) {

        boolean pinned = (compilationDto.getPinned() == null) ? false : compilationDto.getPinned();
        return Compilation.builder()
                .pinned(pinned)
                .title(compilationDto.getTitle())
                .events(new HashSet<>())
                .build();
    }

    public static void updateFromDto(Compilation compilation, NewCompilationDto compilationDto) {

        if (compilationDto.getTitle() != null && !compilationDto.getTitle().isBlank()) {
            compilation.setTitle(compilationDto.getTitle());
        }
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilation.isPinned());
        }
    }
}
