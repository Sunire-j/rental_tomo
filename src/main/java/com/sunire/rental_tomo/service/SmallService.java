package com.sunire.rental_tomo.service;

import com.sunire.rental_tomo.domain.entity.Category;
import com.sunire.rental_tomo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmallService {

    private final CategoryRepository categoryRepository;

    public Map<String ,List<Category>> getAllCategories() {
        Map<String, List<Category>> map = new HashMap<>();
        map.put("parent", categoryRepository.findParentCategories());
        map.put("children", categoryRepository.findChildrenCategories());
        return map;
    }

    public Optional<Category> getCategory(Long id) {
        return categoryRepository.findById(id);
    }


}
