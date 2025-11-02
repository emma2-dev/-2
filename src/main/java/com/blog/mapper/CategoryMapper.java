package com.blog.mapper;

import com.blog.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 分类Mapper
 */
@Mapper
public interface CategoryMapper {
    
    /**
     * 插入分类
     */
    int insert(Category category);
    
    /**
     * 根据ID查询分类
     */
    Category selectById(@Param("id") Long id);
    
    /**
     * 更新分类
     */
    int update(Category category);
    
    /**
     * 删除分类
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 查询所有分类
     */
    List<Category> selectAll();
    
    /**
     * 根据名称查询分类
     */
    Category selectByName(@Param("name") String name);
    
    /**
     * 查询热门分类（按文章数量排序）
     */
    List<Category> selectHotCategories(@Param("limit") Integer limit);
}


