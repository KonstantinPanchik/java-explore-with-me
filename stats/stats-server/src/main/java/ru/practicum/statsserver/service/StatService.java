package ru.practicum.statsserver.service;

import ru.practicum.statsDto.EndpointHitDto;
import ru.practicum.statsDto.ViewStats;
import ru.practicum.statsserver.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    EndpointHit postHit(EndpointHitDto endpointHitDto);

    List<ViewStats> getStat(LocalDateTime start, LocalDateTime end, boolean unique, List<String> uris);
}
