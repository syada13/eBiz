package com.ecommerce.eCom.service;

import com.ecommerce.eCom.model.Category;
import com.ecommerce.eCom.payload.CategoryDTO;
import com.ecommerce.eCom.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories();
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    public String deleteCategory(Long categoryId);
    public Category updateCategory(Category category, Long categoryId);
}
