package ru.practicum.mainservice.event.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Validated
public class AdminEventController {

    EventService eventService;

    @Autowired
    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<Object> getEventsByAdmin(@RequestParam List<Long> users,
                                                   @RequestParam List<State> states,
                                                   @RequestParam List<Long> categories,
                                                   @RequestParam
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                   @RequestParam
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                   @RequestParam(required = false, defaultValue = "0")
                                                   @Min(0) Integer from,
                                                   @RequestParam(required = false, defaultValue = "10")
                                                   @Min(1) Integer size) {

        return ResponseEntity.ok(eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size));

    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> updateByAdmin(@PathVariable Long eventId,
                                                @RequestBody @Valid UpdateEventUserRequest adminRequest) {

        return ResponseEntity.ok(eventService.updateByAdmin(eventId, adminRequest));
    }
}
