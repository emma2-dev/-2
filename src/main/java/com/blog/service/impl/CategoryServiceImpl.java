package com.blog.service.impl;

import com.blog.entity.Category;
import com.blog.mapper.CategoryMapper;
import com.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 分类Service实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Override
    public void createCategory(Category category) {
        Category exist = categoryMapper.selectByName(category.getName());
        if (exist != null) {
            throw new RuntimeException("分类名称已存在");
        }
        categoryMapper.insert(category);
    }
    
    @Override
    public void updateCategory(Category category) {
        categoryMapper.update(category);
    }
    
    @Override
    public void deleteCategory(Long categoryId) {
        categoryMapper.deleteById(categoryId);
    }
    
    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectAll();
    }
    
    @Override
    public List<Category> getHotCategories(Integer limit) {
        return categoryMapper.selectHotCategories(limit);
    }
}


