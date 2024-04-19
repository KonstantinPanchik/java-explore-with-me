package ru.practicum.mainservice.event.service;

import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.event.model.EventSort;
import ru.practicum.mainservice.event.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto addNewEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getAuthorEvents(Long userId, int from, int size);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto getEvent(Long eventId, String ip);

    List<EventShortDto> getPublishedFilteredEvents(String text,
                                                   List<Long> categories,
                                                   Boolean paid,
                                                   LocalDateTime rangeStart,
                                                   LocalDateTime rangeEnd,
                                                   boolean onlyAvailable,
                                                   EventSort eventSort,
                                                   Integer from,
                                                   Integer size,
                                                   String ip,
                                                   String uri);


    List<EventFullDto> getEventsByAdmin(List<Long> users,
                                        List<State> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        int from,
                                        int size);


    EventFullDto updateByAdmin(Long eventId, UpdateEventUserRequest adminRequest);

    List<EventShortDto> getBloggersEvents(Long userId,
                                          Integer from,
                                          Integer size,
                                          LocalDateTime start,
                                          LocalDateTime end);
}