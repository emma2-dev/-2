package com.blog.service.impl;

import com.blog.common.PageResult;
import com.blog.dto.ArticleDTO;
import com.blog.entity.Article;
import com.blog.entity.Tag;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.TagMapper;
import com.blog.service.ArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 文章Service实现类
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    
    @Autowired
    private ArticleMapper articleMapper;
    
    @Autowired
    private TagMapper tagMapper;
    
    @Override
    @Transactional
    public void createArticle(Long userId, ArticleDTO articleDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        article.setUserId(userId);
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCommentCount(0);
        
        // 如果是已发布状态，设置发布时间
        if (article.getStatus() == 2) {
            article.setPublishTime(new Date());
        }
        
        articleMapper.insert(article);
        
        // 保存文章标签关联
        if (articleDTO.getTagIds() != null && !articleDTO.getTagIds().isEmpty()) {
            for (Long tagId : articleDTO.getTagIds()) {
                tagMapper.insertArticleTag(article.getId(), tagId);
            }
        }
    }
    
    @Override
    @Transactional
    public void updateArticle(Long userId, Long articleId, ArticleDTO articleDTO) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        
        BeanUtils.copyProperties(articleDTO, article);
        article.setId(articleId);
        
        // 如果从草稿变为已发布，设置发布时间
        if (article.getStatus() == 2 && article.getPublishTime() == null) {
            article.setPublishTime(new Date());
        }
        
        articleMapper.update(article);
        
        // 更新文章标签关联
        tagMapper.deleteArticleTagsByArticleId(articleId);
        if (articleDTO.getTagIds() != null && !articleDTO.getTagIds().isEmpty()) {
            for (Long tagId : articleDTO.getTagIds()) {
                tagMapper.insertArticleTag(articleId, tagId);
            }
        }
    }
    
    @Override
    public void deleteArticle(Long userId, Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        
        articleMapper.deleteById(articleId);
        tagMapper.deleteArticleTagsByArticleId(articleId);
    }
    
    @Override
    public Article getArticleDetail(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        
        // 增加浏览次数
        articleMapper.increaseViewCount(articleId);
        
        // 查询文章标签
        List<Tag> tags = tagMapper.selectByArticleId(articleId);
        article.setTags(tags);
        
        return article;
    }
    
    @Override
    public Article getArticleById(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        
        // 不增加浏览次数，查询文章标签
        List<Tag> tags = tagMapper.selectByArticleId(articleId);
        article.setTags(tags);
        
        return article;
    }
    
    @Override
    public PageResult<Article> getArticleList(Integer page, Integer limit, Long userId, Long categoryId, Integer status, String keyword) {
        Integer offset = (page - 1) * limit;
        List<Article> articles = articleMapper.selectByPage(offset, limit, userId, categoryId, status, keyword);
        Long total = articleMapper.countArticles(userId, categoryId, status, keyword);
        
        // 查询每篇文章的标签
        for (Article article : articles) {
            List<Tag> tags = tagMapper.selectByArticleId(article.getId());
            article.setTags(tags);
        }
        
        return PageResult.of(total, articles);
    }
    
    @Override
    public List<Article> getRecommendArticles(Integer limit) {
        return articleMapper.selectRecommend(limit);
    }
    
    @Override
    public void likeArticle(Long articleId) {
        articleMapper.increaseLikeCount(articleId);
    }
    
    @Override
    public void auditArticle(Long articleId, Integer status) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        
        article.setStatus(status);
        
        // 如果审核通过，设置发布时间
        if (status == 2 && article.getPublishTime() == null) {
            article.setPublishTime(new Date());
        }
        
        articleMapper.update(article);
    }
}


