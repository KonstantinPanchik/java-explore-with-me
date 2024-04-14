package ru.practicum.mainservice.request.service;

import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.request.model.EventRequestStatusUpdateResult;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getRequestsByEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequests(Long userId, Long eventId,
                                                  EventRequestStatusUpdateRequest statusUpdateRequest);
}
