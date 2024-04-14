package ru.practicum.mainservice.request.dto;

import ru.practicum.mainservice.request.model.Request;

public class RequestMapper {


    public static ParticipationRequestDto toDto(Request request) {

        return ParticipationRequestDto.builder()
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();

    }
}
