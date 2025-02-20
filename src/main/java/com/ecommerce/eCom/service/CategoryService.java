package com.ecommerce.eCom.service;

import com.ecommerce.eCom.model.Category;
import com.ecommerce.eCom.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    public CategoryResponse getAllCategories();
    public void createCategory(Category category);
    public String deleteCategory(Long categoryId);
    public Category updateCategory(Category category, Long categoryId);
}
