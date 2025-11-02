package com.blog.controller;

import com.blog.common.Result;
import com.blog.entity.Category;
import com.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类Controller
 */
@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 创建分类
     */
    @PostMapping
    public Result<String> createCategory(@RequestBody Category category) {
        try {
            categoryService.createCategory(category);
            return Result.success("创建成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新分类
     */
    @PutMapping
    public Result<String> updateCategory(@RequestBody Category category) {
        try {
            categoryService.updateCategory(category);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除分类
     */
    @DeleteMapping("/{categoryId}")
    public Result<String> deleteCategory(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取所有分类
     */
    @GetMapping("/list")
    public Result<List<Category>> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取热门分类
     */
    @GetMapping("/hot")
    public Result<List<Category>> getHotCategories(@RequestParam(defaultValue = "5") Integer limit) {
        try {
            List<Category> categories = categoryService.getHotCategories(limit);
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

