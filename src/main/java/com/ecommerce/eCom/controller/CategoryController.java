package com.ecommerce.eCom.controller;

import com.ecommerce.eCom.model.Category;
import com.ecommerce.eCom.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
   private CategoryService categoryService;
   private Long nextId = 1L;

   public CategoryController(CategoryService categoryService){
       this.categoryService = categoryService;
   }

    @RequestMapping(value="/public/categories",method=RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategories(){
       List<Category> categories = categoryService.getAllCategories();
       return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @RequestMapping(value="/public/categories",method=RequestMethod.POST)
    public ResponseEntity<String> createCategory(@RequestBody Category category){
        category.setCategoryId(nextId++);
        categoryService.createCategory(category);
        return new ResponseEntity<>("New category added successfully",HttpStatus.CREATED);
    }

    @RequestMapping(value="/admin/categories/{categoryId}",method=RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
       try{
           String status = categoryService.deleteCategory(categoryId);
           return new ResponseEntity<>(status, HttpStatus.OK);
       }catch(ResponseStatusException e){
           return new ResponseEntity<>(e.getReason(),e.getStatusCode());
       }
    }

    @RequestMapping(value="/public/categories/{categoryId}",method=RequestMethod.PUT)
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId){
       try{
           Category categorySaved = categoryService.updateCategory(category,categoryId);
           return new ResponseEntity<>("Category updated successfully",HttpStatus.OK);
       }catch(ResponseStatusException e){
           return new ResponseEntity<>(e.getReason(),e.getStatusCode());
       }
    }
}
