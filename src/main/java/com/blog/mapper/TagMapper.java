package com.blog.mapper;

import com.blog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 标签Mapper
 */
@Mapper
public interface TagMapper {
    
    /**
     * 插入标签
     */
    int insert(Tag tag);
    
    /**
     * 根据ID查询标签
     */
    Tag selectById(@Param("id") Long id);
    
    /**
     * 更新标签
     */
    int update(Tag tag);
    
    /**
     * 删除标签
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 查询所有标签
     */
    List<Tag> selectAll();
    
    /**
     * 根据文章ID查询标签列表
     */
    List<Tag> selectByArticleId(@Param("articleId") Long articleId);
    
    /**
     * 插入文章标签关联
     */
    int insertArticleTag(@Param("articleId") Long articleId, @Param("tagId") Long tagId);
    
    /**
     * 删除文章的所有标签关联
     */
    int deleteArticleTagsByArticleId(@Param("articleId") Long articleId);
    
    /**
     * 查询热门标签（按使用次数排序）
     */
    List<Tag> selectHotTags(@Param("limit") Integer limit);
}


