package ru.practicum.main_service.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CategoryDto {
    private Long id;
    @NotBlank
    private String name;
}
