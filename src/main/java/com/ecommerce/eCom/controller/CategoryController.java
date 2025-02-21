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
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
    }

    @RequestMapping(value="/admin/categories/{categoryId}",method=RequestMethod.DELETE)
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
           CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);
           return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);
    }

    @RequestMapping(value="/public/categories/{categoryId}",method=RequestMethod.PUT)
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){
           CategoryDTO categorySavedDTO = categoryService.updateCategory(categoryDTO,categoryId);
           return new ResponseEntity<>(categorySavedDTO,HttpStatus.OK);
    }
}
