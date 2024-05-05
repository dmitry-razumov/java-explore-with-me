package ru.practicum.main_service.category.service;

import ru.practicum.main_service.category.model.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category);

    Category updateCategory(Category category, Long catId);

    void deleteCategory(Long catId);

    List<Category> getCategories(int from, int size);

    Category getCategory(Long catId);
}
