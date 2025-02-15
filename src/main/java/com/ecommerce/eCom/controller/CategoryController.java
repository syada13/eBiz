package com.ecommerce.eCom.controller;

import com.ecommerce.eCom.model.Category;
import com.ecommerce.eCom.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
   private CategoryService categoryService;
   private Long nextId = 1L;

   public CategoryController(CategoryService categoryService){
       this.categoryService = categoryService;
   }

    @GetMapping("/api/public/categories")
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }


    @PostMapping("/api/public/categories")
    public String createCategory(@RequestBody Category category){
        category.setCategoryId(nextId++);
        categoryService.createCategory(category);
        return "New category added successfully";

    }

    @DeleteMapping("/api/public/categories/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId){
        return categoryService.deleteCategory(categoryId);
    }
}
