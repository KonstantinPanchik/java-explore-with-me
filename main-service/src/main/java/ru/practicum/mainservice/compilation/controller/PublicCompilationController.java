package ru.practicum.mainservice.compilation.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class PublicCompilationController {

    private final CompilationService compilationService;


    @GetMapping
    public ResponseEntity<Object> getCompilation(@RequestParam(required = false, defaultValue = "0")
                                                 @Min(value = 0) Integer from,
                                                 @RequestParam(required = false, defaultValue = "10")
                                                 @Min(value = 10) Integer size,
                                                 @RequestParam(required = false) Boolean pinned) {


        return ResponseEntity.ok(compilationService.getAllCompilations(from, size, pinned));
    }

    @GetMapping("/{compId}")
    public ResponseEntity<Object> getCompilation(@PathVariable Long compId) {


        return ResponseEntity.ok(compilationService.getCompilationById(compId));
    }
}
