package ru.practicum.mainservice.category.service;

import ru.practicum.mainservice.category.model.Category;

import java.util.List;

public interface CategoryService {

    Category addNew(Category category);

    Category update(Category category, Long id);

    void delete(Long id);

    List<Category> getAll(int from, int size);

    Category get(Long id);

    List<Category> getIn(List<Long> ids);

}
