package com.blog.mapper;

import com.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 文章Mapper
 */
@Mapper
public interface ArticleMapper {
    
    /**
     * 插入文章
     */
    int insert(Article article);
    
    /**
     * 根据ID查询文章
     */
    Article selectById(@Param("id") Long id);
    
    /**
     * 更新文章
     */
    int update(Article article);
    
    /**
     * 删除文章
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 分页查询文章列表
     */
    List<Article> selectByPage(@Param("offset") Integer offset,
                               @Param("limit") Integer limit,
                               @Param("userId") Long userId,
                               @Param("categoryId") Long categoryId,
                               @Param("status") Integer status,
                               @Param("keyword") String keyword);
    
    /**
     * 统计文章数量
     */
    Long countArticles(@Param("userId") Long userId,
                       @Param("categoryId") Long categoryId,
                       @Param("status") Integer status,
                       @Param("keyword") String keyword);
    
    /**
     * 增加浏览次数
     */
    int increaseViewCount(@Param("id") Long id);
    
    /**
     * 增加点赞次数
     */
    int increaseLikeCount(@Param("id") Long id);
    
    /**
     * 查询推荐文章
     */
    List<Article> selectRecommend(@Param("limit") Integer limit);
}


