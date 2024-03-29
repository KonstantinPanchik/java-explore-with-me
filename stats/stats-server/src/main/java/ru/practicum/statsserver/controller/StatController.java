package ru.practicum.statsserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsDto.EndpointHitDto;
import ru.practicum.statsDto.ViewStats;
import ru.practicum.statsserver.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
public class StatController {


    StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> postHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info(endpointHitDto.toString());
        return ResponseEntity.ok(statService.postHit(endpointHitDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStats>> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                    @RequestParam(required = false, defaultValue = "false") Boolean unique,
                                                    @RequestParam(required = false) List<String> uris) {
        log.info("Statistic from {} to {} uniq = {}  uris = {} ",
                start, end, unique, uris);

        return ResponseEntity.ok(statService.getStat(start, end, unique, uris));
    }
}
