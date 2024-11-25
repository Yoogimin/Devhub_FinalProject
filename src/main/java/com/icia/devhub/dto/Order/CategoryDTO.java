package com.icia.devhub.dto.Order;

import lombok.Data;

@Data
public class CategoryDTO {
    private int category;
    private String categoryName;

    public static CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();

        dto.setCategory(entity.getCategory());
        dto.setCategoryName(entity.getCategoryName());

        return dto;
    }
}