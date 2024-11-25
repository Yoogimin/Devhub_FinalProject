package com.icia.devhub.Service;

import com.icia.devhub.dao.CategoryRepository;
import com.icia.devhub.dto.Order.CategoryDTO;
import com.icia.devhub.dto.Order.CategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository crepo;

    public List<CategoryDTO> CategoryList(int category) {
        List<CategoryDTO> dtoList = new ArrayList<>();
        List<CategoryEntity> entityList = crepo.findAllBycategory(category);

        for (CategoryEntity entity : entityList) {
            dtoList.add(CategoryDTO.toDTO(entity));
        }
        return dtoList;
    }
}
