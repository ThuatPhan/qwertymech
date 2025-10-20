package org.example.productservice.service;

import java.util.List;

import org.example.productservice.dto.request.CategoryCreationRequest;
import org.example.productservice.dto.request.CategoryUpdateRequest;
import org.example.productservice.dto.response.CategoryResponse;
import org.example.productservice.entity.Category;
import org.example.productservice.exception.AppException;
import org.example.productservice.exception.ErrorCode;
import org.example.productservice.mapper.CategoryMapper;
import org.example.productservice.repository.CategoryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(CategoryCreationRequest request) {
        boolean isCategoryExists = categoryRepository.existsByName(request.getName());
        if (isCategoryExists) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category newCategory = categoryMapper.toCategory(request);
        Category savedCategory = categoryRepository.save(newCategory);
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    public CategoryResponse getCategory(String id) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::toCategoryResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(String id, CategoryUpdateRequest request) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        boolean isCategoryExists = categoryRepository.existsByNameAndIdNot(id, request.getName());
        if (isCategoryExists) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        Category updatedCategory = categoryMapper.updateCategory(request, category);
        Category savedCategory = categoryRepository.save(updatedCategory);
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(String id) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }
}
