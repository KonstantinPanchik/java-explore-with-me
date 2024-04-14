package ru.practicum.mainservice.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.service.CategoryService;

@RestController
@RequestMapping("/categories")
@Slf4j
public class PublicCategoryController {

    CategoryService categoryService;

    @Autowired
    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(required = false, defaultValue = "0") Integer from,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {

        return ResponseEntity.ok(categoryService.getAll(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> getCategory(@PathVariable Long catId) {

        return ResponseEntity.ok(categoryService.get(catId));
    }
}
