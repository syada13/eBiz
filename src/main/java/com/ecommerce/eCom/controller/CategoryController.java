package com.ecommerce.eCom.controller;

import com.ecommerce.eCom.model.Category;
import com.ecommerce.eCom.payload.CategoryDTO;
import com.ecommerce.eCom.payload.CategoryResponse;
import com.ecommerce.eCom.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
   private CategoryService categoryService;

   public CategoryController(CategoryService categoryService){
       this.categoryService = categoryService;
   }
    @RequestMapping(value="/public/categories",method=RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories(){
       CategoryResponse categoryResponse = categoryService.getAllCategories();
       return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }

    @RequestMapping(value="/public/categories",method=RequestMethod.POST)
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>("New category added successfully",HttpStatus.CREATED);
    }

    @RequestMapping(value="/admin/categories/{categoryId}",method=RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
           String status = categoryService.deleteCategory(categoryId);
           return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @RequestMapping(value="/public/categories/{categoryId}",method=RequestMethod.PUT)
    public ResponseEntity<String> updateCategory(@Valid @RequestBody Category category, @PathVariable Long categoryId){
           Category categorySaved = categoryService.updateCategory(category,categoryId);
           return new ResponseEntity<>("Category updated successfully",HttpStatus.OK);
    }
}
