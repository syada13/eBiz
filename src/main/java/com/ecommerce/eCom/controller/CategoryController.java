package com.ecommerce.eCom.controller;

import com.ecommerce.eCom.config.AppConstants;
import com.ecommerce.eCom.model.Category;
import com.ecommerce.eCom.payload.CategoryDTO;
import com.ecommerce.eCom.payload.CategoryResponse;
import com.ecommerce.eCom.payload.ProductResponse;
import com.ecommerce.eCom.service.CategoryService;
import com.ecommerce.eCom.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
   private CategoryService categoryService;
   private ProductService productService;

   public CategoryController(CategoryService categoryService){
       this.categoryService = categoryService;
   }
    @RequestMapping(value="/public/categories",method=RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam (name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam (name="pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam (name="sortBy", defaultValue = AppConstants.SORT_BY,required=false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder){
       CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
       return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/public/categories",method=RequestMethod.POST)
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/admin/categories/{categoryId}",method=RequestMethod.DELETE)
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
           CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);
           return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/public/categories/{categoryId}",method=RequestMethod.PUT)
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){
           CategoryDTO categorySavedDTO = categoryService.updateCategory(categoryDTO,categoryId);
           return new ResponseEntity<>(categorySavedDTO,HttpStatus.OK);
    }
}
