package ru.practicum.mainservice.request.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.dto.RequestMapper;
import ru.practicum.mainservice.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.request.model.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.repository.RequestRepository;
import ru.practicum.mainservice.request.service.RequestService;
import ru.practicum.mainservice.request.status.RequestStatus;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImp implements RequestService {

    @Autowired
    public RequestServiceImp(RequestRepository requestRepository, UserService userService, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.eventRepository = eventRepository;
    }

    RequestRepository requestRepository;
    UserService userService;
    EventRepository eventRepository;

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));
        User user = userService.getById(userId);

        if (!user.equals(request.getRequester())) {
            throw new ConflictException("You can't cancel this request");
        }

        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }

        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);

        return RequestMapper.toDto(request);
    }

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {

        User requester = userService.getById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found")
        );

        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("You cant partisapate in your event");
        }

        if (!(event.getState().equals(State.PUBLISHED))) {
            throw new ConflictException("Event must be published");
        }

        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ConflictException("ParticipantLimit is over");
        }

        RequestStatus requestStatus = event.isRequestModeration()
                ? RequestStatus.PENDING : RequestStatus.CONFIRMED;

        Request newRequest = Request.builder()
                .requester(requester)
                .event(event)
                .created(LocalDateTime.now())
                .status(requestStatus)
                .build();

        newRequest = requestRepository.save(newRequest);

        if (requestStatus.equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }


        return RequestMapper.toDto(newRequest);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        User requester = userService.getById(userId);

        return requestRepository.findAllByRequester(requester)
                .stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        User user = userService.getById(userId);

        if (!user.equals(event.getInitiator())) {
            throw new ConflictException("You can't cancel this request");
        }
        return requestRepository.findAllByEvent(event)
                .stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(Long userId, Long eventId,
                                                         EventRequestStatusUpdateRequest statusUpdateRequest) {

        if (statusUpdateRequest.getStatus().equals(RequestStatus.CANCELED) ||
                statusUpdateRequest.getStatus().equals(RequestStatus.PENDING)) {
            throw new RuntimeException("Status is not correct");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        User user = userService.getById(userId);

        if (!user.equals(event.getInitiator())) {
            throw new ConflictException("You can't cancel this request");
        }

        List<Request> requests = requestRepository.findAllById(statusUpdateRequest.getRequestIds());

        if (!isRequestPending(requests)) {
            throw new ConflictException("Request status must be Pending");
        }

        if (statusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            int limit = event.getParticipantLimit();
            int alreadyConfirmed = event.getConfirmedRequests();
            int updateRequestSize = statusUpdateRequest.getRequestIds().size();

            if (limit > 0 && limit < alreadyConfirmed + updateRequestSize) {
                throw new ConflictException("Participant limit is over");
            }

            setStatus(requests, RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + updateRequestSize);
            eventRepository.save(event);
            alreadyConfirmed = event.getConfirmedRequests();

            if (limit > 0 && alreadyConfirmed == limit) {
                List<Request> anotherPending = requestRepository
                        .findAllByEventAndStatus(event, RequestStatus.PENDING);
                setStatus(anotherPending, RequestStatus.REJECTED);
            }

        } else {
            setStatus(requests, RequestStatus.REJECTED);
        }

        List<ParticipationRequestDto> confirmed = requestRepository
                .findAllByEventAndStatus(event, RequestStatus.CONFIRMED)
                .stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());

        List<ParticipationRequestDto> rejected = requestRepository
                .findAllByEventAndStatus(event, RequestStatus.REJECTED)
                .stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed)
                .rejectedRequests(rejected)
                .build();


    }

    private boolean isRequestPending(List<Request> requests) {

        boolean result = true;
        for (Request request : requests) {

            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                result = false;
                break;
            }
        }
        return result;
    }

    private void setStatus(List<Request> requests, RequestStatus status) {

        for (Request request : requests) {
            request.setStatus(status);
            requestRepository.save(request);

        }
    }
}
