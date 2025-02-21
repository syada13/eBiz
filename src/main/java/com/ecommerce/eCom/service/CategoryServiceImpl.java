package com.ecommerce.eCom.service;

import com.ecommerce.eCom.exceptions.APIException;
import com.ecommerce.eCom.exceptions.ResourceNotFoundException;
import com.ecommerce.eCom.model.Category;
import com.ecommerce.eCom.payload.CategoryDTO;
import com.ecommerce.eCom.payload.CategoryResponse;
import com.ecommerce.eCom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new APIException("No category created yet.");
        }

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class) )
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO){
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category savedCategoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if (savedCategoryFromDB !=null){
            throw new APIException("Category with name: "+ category.getCategoryName() + " already exists!!!");
        }
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory,CategoryDTO.class);
        return savedCategoryDTO;
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
