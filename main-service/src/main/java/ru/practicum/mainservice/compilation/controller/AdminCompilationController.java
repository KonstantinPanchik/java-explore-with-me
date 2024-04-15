package ru.practicum.mainservice.compilation.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;


    @PostMapping
    public ResponseEntity<Object> addCompilation(@RequestBody @Valid NewCompilationDto newCompilation) {

        return ResponseEntity.status(HttpStatus.CREATED).body(compilationService.addCompilation(newCompilation));
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<Object> updateCompilation(@PathVariable Long compId,
                                                    @RequestBody @Valid NewCompilationDto newCompilation) {

        return ResponseEntity.ok(compilationService.updateCompilation(compId, newCompilation));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable Long compId) {

        compilationService.deleteCompilation(compId);

        return ResponseEntity.noContent().build();
    }


}
