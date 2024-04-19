package ru.practicum.mainservice.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@RestController
@RequestMapping("users/{userId}/subscriptions")
@AllArgsConstructor
public class PrivateUserController {

    UserService userService;
    EventService eventService;

    @PostMapping("/{bloggerId}")
    public ResponseEntity<Object> subscribe(@PathVariable Long userId,
                                            @PathVariable Long bloggerId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.subscribe(userId, bloggerId));
    }

    @DeleteMapping("/{bloggerId}")
    public ResponseEntity<Object> unSubscribe(@PathVariable Long userId,
                                              @PathVariable Long bloggerId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.unSubscribe(userId, bloggerId));
    }

    @GetMapping
    public ResponseEntity<Object> getBloggerEvents(@PathVariable Long userId,
                                                   @RequestParam(required = false, defaultValue = "0")
                                                   @Min(0) Integer from,
                                                   @RequestParam(required = false, defaultValue = "10")
                                                   @Min(1) Integer size,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd) {

        return ResponseEntity.ok(eventService.getBloggersEvents(userId, from, size, rangeStart, rangeEnd));
    }
}
