package ru.practicum.mainservice.category.service.imp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImp implements CategoryService {

    CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category addNew(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category, Long id) {
        Category savedCategory = get(id);
        log.debug("Category found");
        savedCategory.setName(category.getName());
        categoryRepository.save(savedCategory);

        return categoryRepository.save(savedCategory);
    }

    @Override
    public String delete(Long id) {
        Category category = get(id);
        log.debug("Category found");

        try {
            categoryRepository.delete(category);
            log.debug("Category deleted");
            return "Категория удалена";

        } catch (Throwable throwable) {
            log.debug("Category conflict");
            throw new ConflictException("The category is not empty");
        }

    }

    @Override
    public List<Category> getAll(int from, int size) {
        Page<Category> page = categoryRepository.findAll(PageRequest.of(from / size, size, Sort.by("id")));
        return page.stream().collect(Collectors.toList());
    }

    @Override
    public Category get(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id=" + id + " was not found"));
    }

    @Override
    public List<Category> getIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return categoryRepository.findAll();
        } else {
            return categoryRepository.findAllById(ids);
        }
    }
}
