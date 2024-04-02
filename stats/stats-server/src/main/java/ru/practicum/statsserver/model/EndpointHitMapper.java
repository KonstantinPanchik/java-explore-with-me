package ru.practicum.statsserver.model;

import ru.practicum.statsDto.EndpointHitDto;

public class EndpointHitMapper {

    public static EndpointHit from(EndpointHitDto dto) {

        return EndpointHit.builder()
                .app(dto.getApp())
                .ip(dto.getIp())
                .uri(dto.getUri())
                .timestamp(dto.getTimestamp())
                .build();
    }
}
