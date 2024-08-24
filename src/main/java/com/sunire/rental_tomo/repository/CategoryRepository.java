package com.sunire.rental_tomo.repository;

import com.sunire.rental_tomo.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
