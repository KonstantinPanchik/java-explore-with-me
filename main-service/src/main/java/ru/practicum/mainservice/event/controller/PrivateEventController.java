package ru.practicum.mainservice.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.request.model.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.request.service.RequestService;

import javax.validation.Valid;

@RestController
@RequestMapping("users/{userId}/events")
@Validated
public class PrivateEventController {


    EventService eventService;
    RequestService requestService;

    @Autowired
    public PrivateEventController(EventService eventService, RequestService requestService) {
        this.eventService = eventService;
        this.requestService = requestService;
    }


    @PostMapping
    public ResponseEntity<Object> addEvent(@PathVariable Long userId,
                                           @RequestBody @Validated NewEventDto newEventDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.addNewEvent(userId, newEventDto));

    }

    @GetMapping
    public ResponseEntity<Object> getUsersEvents(@PathVariable Long userId,
                                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(eventService.getAuthorEvents(userId, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable Long userId,
                                              @PathVariable Long eventId,
                                              @RequestBody @Validated UpdateEventUserRequest request) {

        return ResponseEntity.ok(eventService.updateEvent(userId, eventId, request));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getOneUserEvent(@PathVariable Long userId,
                                                  @PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(userId, eventId));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<Object> getRequestsByUserEvent(@PathVariable Long userId,
                                                         @PathVariable Long eventId) {
        return ResponseEntity.ok(requestService.getRequestsByEvent(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<Object> confirmRequests(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest) {
        return ResponseEntity.ok(requestService.updateRequests(userId, eventId, updateRequest));

    }

}
