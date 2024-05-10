package ru.practicum.main_service.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.dto.NewCategoryDto;
import ru.practicum.main_service.category.mapper.CategoryMapper;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.repository.CategoryRepository;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.utils.PageRequestCustom;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category newCategory = categoryRepository.save(categoryMapper.toCategory(newCategoryDto));
        log.info("создана категория {}", newCategory);
        return categoryMapper.toCategoryDto(newCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(NewCategoryDto newCategoryDto, Long catId) {
        Category updatedCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
        updatedCategory.setName(categoryMapper.toCategory(newCategoryDto).getName());
        log.info("обновлена категория {}", updatedCategory);
        return categoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
        categoryRepository.deleteById(catId);
        log.info("удалена категория c id={}", catId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable page = PageRequestCustom.get(from, size);
        List<Category> categories = categoryRepository.findAllByOrderById(page);
        log.info("получены категории {}", categories);
        return categoryMapper.toCategoryDto(categories);
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
        log.info("получена категория {} c id={}", category, catId);
        return categoryMapper.toCategoryDto(category);
    }
}
