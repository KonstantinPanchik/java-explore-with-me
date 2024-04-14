package ru.practicum.mainservice.event.dto;

import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.Optional;

public class EventMapper {

    public static EventShortDto toShortDto(Event event) {

        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .build();
    }


    public static Event toEvent(NewEventDto newEventDto) {

        boolean paid = Optional.ofNullable(newEventDto.getPaid()).orElse(false);
        boolean requestModeration = Optional
                .ofNullable(newEventDto.getRequestModeration()).orElse(false);

        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .title(newEventDto.getTitle())
                .paid(paid)
                .requestModeration(requestModeration)
                .participantLimit(newEventDto.getParticipantLimit())
                .build();
    }

    public static Event updateEvent(Event event, UpdateEventUserRequest update) {

        if (update.getAnnotation() != null
                && !(update.getAnnotation().isBlank())) {
            event.setAnnotation(update.getAnnotation());
        }

        if (update.getDescription() != null
                && !(update.getDescription().isBlank())) {
            event.setDescription(update.getDescription());
        }

        if (update.getTitle() != null
                && !(update.getTitle().isBlank())) {
            event.setTitle(update.getTitle());
        }

        if (update.getEventDate() != null) {
            event.setEventDate(update.getEventDate());
        }

        if (update.getPaid() != null) {
            event.setPaid(update.getPaid());
        }

        if (update.getRequestModeration() != null) {
            event.setRequestModeration(update.getRequestModeration());
        }

        if (update.getParticipantLimit() != null) {
            event.setParticipantLimit(update.getParticipantLimit());
        }

        if (update.getStateAction() != null) {

            switch (update.getStateAction()) {

                case CANCEL_REVIEW:
                case REJECT_EVENT:
                    event.setState(State.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case PUBLISH_EVENT:
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());

            }
        }

        return event;
    }

    public static EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .id(event.getId())
                .category(event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .participantLimit(event.getParticipantLimit())
                .location(event.getLocation())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .paid(event.isPaid())
                .title(event.getTitle())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .publishedOn(event.getPublishedOn())
                .build();

    }
}
