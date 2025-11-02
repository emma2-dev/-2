package com.blog.service;

import com.blog.common.PageResult;
import com.blog.dto.ArticleDTO;
import com.blog.entity.Article;

import java.util.List;

/**
 * 文章Service接口
 */
public interface ArticleService {
    
    /**
     * 创建文章
     */
    void createArticle(Long userId, ArticleDTO articleDTO);
    
    /**
     * 更新文章
     */
    void updateArticle(Long userId, Long articleId, ArticleDTO articleDTO);
    
    /**
     * 删除文章
     */
    void deleteArticle(Long userId, Long articleId);
    
    /**
     * 获取文章详情
     */
    Article getArticleDetail(Long articleId);
    
    /**
     * 根据ID查询文章（不增加浏览次数）
     */
    Article getArticleById(Long articleId);
    
    /**
     * 分页查询文章列表
     */
    PageResult<Article> getArticleList(Integer page, Integer limit, Long userId, Long categoryId, Integer status, String keyword);
    
    /**
     * 获取推荐文章
     */
    List<Article> getRecommendArticles(Integer limit);
    
    /**
     * 文章点赞
     */
    void likeArticle(Long articleId);
    
    /**
     * 审核文章
     */
    void auditArticle(Long articleId, Integer status);
}


