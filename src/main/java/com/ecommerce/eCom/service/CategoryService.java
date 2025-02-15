package com.ecommerce.eCom.service;

import com.ecommerce.eCom.model.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> getAllCategories();
    public void createCategory(Category category);
    public String deleteCategory(Long categoryId);
}
