package ru.practicum.mainservice.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.model.EventSort;
import ru.practicum.mainservice.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")

public class PublicEventController {

    EventService eventService;

    @Autowired
    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        String ip = request.getRemoteAddr();

        return ResponseEntity.ok(eventService.getEvent(eventId, ip));
    }


    @GetMapping
    public ResponseEntity<Object> searchEvents(@RequestParam(required = false, defaultValue = "") String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                               @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(name = "sort") EventSort eventSort,
                                               @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                               @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size,
                                               HttpServletRequest request) throws ServletRequestBindingException {

        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {

            throw new ServletRequestBindingException("[rangeStart-rangeEnd]");
        }

        String ip = request.getRemoteAddr();

        return ResponseEntity.ok(eventService.getPublishedFilteredEvents(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                eventSort,
                from,
                size,
                ip));
    }


}
