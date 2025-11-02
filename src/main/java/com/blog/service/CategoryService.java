package com.blog.service;

import com.blog.entity.Category;
import java.util.List;

/**
 * 分类Service接口
 */
public interface CategoryService {
    
    /**
     * 创建分类
     */
    void createCategory(Category category);
    
    /**
     * 更新分类
     */
    void updateCategory(Category category);
    
    /**
     * 删除分类
     */
    void deleteCategory(Long categoryId);
    
    /**
     * 获取所有分类
     */
    List<Category> getAllCategories();
    
    /**
     * 获取热门分类
     */
    List<Category> getHotCategories(Integer limit);
}


