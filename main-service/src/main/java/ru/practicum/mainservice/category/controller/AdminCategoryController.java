package ru.practicum.mainservice.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
public class AdminCategoryController {

    CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Object> addNewCategory(@RequestBody @Valid Category category) {

        log.info("Post request addCategory");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.addNew(category));
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<Object> updateCategory(@RequestBody @Valid Category category,
                                                 @PathVariable Long catId) {

        return ResponseEntity.ok(categoryService.update(category, catId));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long catId) {
        return ResponseEntity.ok(categoryService.delete(catId));
    }

}
