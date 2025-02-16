package com.ecommerce.eCom.repositories;

import com.ecommerce.eCom.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
