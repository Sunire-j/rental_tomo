package com.sunire.rental_tomo.repository;

import com.sunire.rental_tomo.domain.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAll(Sort sort);

    @Query("select c from Category c where c.id>=100 order by c.id")
    List<Category> findParentCategories();

    @Query("select c from Category c where c.id<100 order by c.id")
    List<Category> findChildrenCategories();
}
