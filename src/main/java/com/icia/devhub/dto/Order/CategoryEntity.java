package com.icia.devhub.dto.Order;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="DEV_CATEGORY")
@SequenceGenerator(name="DCG_SEQ_GENERATOR", sequenceName="DCG_SEQ", allocationSize=1)
public class CategoryEntity {
    //카테고리 ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCG_SEQ_GENERATOR")
    private int category;

    //카테고리 이름
    @Column
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<ProductEntity> projects;

    public static CategoryEntity toEntity(CategoryDTO dto) {
        CategoryEntity category = new CategoryEntity();

        category.setCategory(dto.getCategory());
        category.setCategoryName(dto.getCategoryName());

        return category;
    }
}