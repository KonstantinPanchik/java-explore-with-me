package ru.practicum.mainservice.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.request.service.RequestService;

@RestController
@RequestMapping("users/{userId}/requests")
public class RequestController {


    RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(@PathVariable Long userId,
                                             @RequestParam Long eventId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.addRequest(userId, eventId));
    }

    @GetMapping
    public ResponseEntity<Object> getRequest(@PathVariable Long userId) {

        return ResponseEntity.ok(requestService.getRequests(userId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable Long userId,
                                                @PathVariable Long requestId) {

        return ResponseEntity.ok(requestService.cancelRequest(userId, requestId));
    }

}
