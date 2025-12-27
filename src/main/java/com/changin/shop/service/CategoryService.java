package com.changin.shop.service;

import com.changin.shop.dto.CategoryDto;
import com.changin.shop.entity.Category;
import com.changin.shop.repository.CategoryRepository;
import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    public void saveCategory(CategoryDto categoryDto) {
        categoryRepository.save(categoryDto.toEntity());
    }

    public List<CategoryDto> getCategories() {
        List<CategoryDto> categoryDtoList = categoryRepository.findAll().stream()
                .map(e-> {
                    return CategoryDto.builder()
                            .categoryId(e.getId())
                            .categoryNm(e.getCategoryNm())
                            .build();
                }).toList();

        return new ArrayList<>(categoryDtoList);
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(EntityNotFoundException::new);
        categoryRepository.deleteById(categoryId);
    }

    public void updateCategory(Long categoryId, String name) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(EntityNotFoundException::new);
        category.updateName(name);
    }
}
