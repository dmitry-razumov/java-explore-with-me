package ru.practicum.main_service.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.dto.NewCategoryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category toCategory(NewCategoryDto newCategoryDto);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDto(List<Category> category);
}
