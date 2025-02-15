package com.ecommerce.eCom.service;

import com.ecommerce.eCom.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    private List<Category> categories = new ArrayList<>();

    public List<Category> getAllCategories() {
        return categories;
    }

    public void createCategory(Category category){
        categories.add(category);
    }

    public String deleteCategory(Long categoryId){
       Category category =  categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
               .findFirst().orElse(null);
       if(category == null) return " Category not found";

       categories.remove(category);
       return "Category with a categoryId: "+ categoryId + "deleted successfully !!!";
    }
}
