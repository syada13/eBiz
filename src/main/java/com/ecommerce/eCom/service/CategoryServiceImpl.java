package com.ecommerce.eCom.service;

import com.ecommerce.eCom.exceptions.APIException;
import com.ecommerce.eCom.exceptions.ResourceNotFoundException;
import com.ecommerce.eCom.model.Category;
import com.ecommerce.eCom.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void createCategory(Category category){
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategory !=null){
            throw new APIException("Category with name: "+ category.getCategoryName() + " already exists!!!");
        }
        categoryRepository.save(category);
    }

    public String deleteCategory(Long categoryId){
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        categoryRepository.delete(savedCategory);
        return "Category with a categoryId: "+ categoryId + " deleted successfully.";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

         Category savedCategory = categoryRepository.findById(categoryId)
                 .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

         savedCategory.setCategoryName(category.getCategoryName());
         categoryRepository.save(savedCategory);
         return savedCategory;
    }
}
