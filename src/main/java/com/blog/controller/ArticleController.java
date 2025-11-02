package com.blog.controller;

import com.blog.common.PageResult;
import com.blog.common.Result;
import com.blog.dto.ArticleDTO;
import com.blog.entity.Article;
import com.blog.mapper.ArticleMapper;
import com.blog.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章Controller
 */
@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private ArticleMapper articleMapper;
    
    /**
     * 创建文章
     */
    @PostMapping
    public Result<String> createArticle(HttpServletRequest request, 
                                     @Validated @RequestBody ArticleDTO articleDTO) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");
            
            logger.info("创建文章请求 - userId: {}, role: {}, status: {}", userId, role, articleDTO.getStatus());
            
            // 如果是普通用户，且要求发布，需要改为待审核状态
            if (!"ADMIN".equals(role) && articleDTO.getStatus() == 2) {
                articleDTO.setStatus(1);  // 改为待审核
            }
            
            articleService.createArticle(userId, articleDTO);
            
            String msg = "ADMIN".equals(role) ? "创建成功" : "文章已提交，等待管理员审核";
            return Result.success(msg);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新文章
     */
    @PutMapping("/{articleId}")
    public Result<String> updateArticle(HttpServletRequest request,
                                     @PathVariable Long articleId,
                                     @Validated @RequestBody ArticleDTO articleDTO) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");
            
            logger.info("编辑文章请求 - userId: {}, role: {}, articleId: {}", userId, role, articleId);
            
            // 管理员可以编辑任何文章；普通用户只能编辑自己的文章
            // 先查询文章（不增加浏览次数）
            Article existingArticle = articleMapper.selectById(articleId);
            if (existingArticle == null) {
                return Result.error(404, "文章不存在");
            }
            
            // 允许管理员或作者编辑
            if (!"ADMIN".equals(role) && !existingArticle.getUserId().equals(userId)) {
                logger.warn("权限不足 - role: {}, userId: {}, articleAuthorId: {}", role, userId, existingArticle.getUserId());
                return Result.error(403, "无权操作");
            }
            
            // 如果是普通用户，且要求发布，需要改为待审核状态
            if (!"ADMIN".equals(role) && articleDTO.getStatus() == 2) {
                articleDTO.setStatus(1);  // 改为待审核
            }
            
            articleService.updateArticle(userId, articleId, articleDTO);
            
            String msg = "ADMIN".equals(role) ? "更新成功" : "文章已提交，等待管理员审核";
            return Result.success(msg);
        } catch (Exception e) {
            logger.error("编辑文章失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除文章
     */
    @DeleteMapping("/{articleId}")
    public Result<String> deleteArticle(HttpServletRequest request, @PathVariable Long articleId) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            String role = (String) request.getAttribute("role");
            
            logger.info("删除文章请求 - userId: {}, role: {}, articleId: {}", userId, role, articleId);
            
            // 管理员可以删除任何文章；普通用户只能删除自己的文章
            Article existingArticle = articleMapper.selectById(articleId);
            if (existingArticle == null) {
                return Result.error(404, "文章不存在");
            }
            if (!"ADMIN".equals(role) && !existingArticle.getUserId().equals(userId)) {
                logger.warn("权限不足 - role: {}, userId: {}, articleAuthorId: {}", role, userId, existingArticle.getUserId());
                return Result.error(403, "无权操作");
            }
            
            articleService.deleteArticle(userId, articleId);
            return Result.success("删除成功");
        } catch (Exception e) {
            logger.error("删除文章失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取文章详情（前端查看用，会增加浏览次数）
     */
    @GetMapping("/{articleId}")
    public Result<Article> getArticleDetail(@PathVariable Long articleId) {
        try {
            Article article = articleService.getArticleDetail(articleId);
            return Result.success(article);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取文章信息（编辑用，不增加浏览次数）
     */
    @GetMapping("/{articleId}/info")
    public Result<Article> getArticleInfo(@PathVariable Long articleId) {
        try {
            Article article = articleService.getArticleById(articleId);
            return Result.success(article);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 管理员审核文章
     */
    @PutMapping("/{articleId}/audit")
    public Result<String> auditArticle(HttpServletRequest request,
                                      @PathVariable Long articleId,
                                      @RequestParam Integer status) {
        try {
            String role = (String) request.getAttribute("role");
            if (!"ADMIN".equals(role)) {
                return Result.error(403, "只有管理员才能审核文章");
            }
            
            // status: 2-已发布，3-审核未通过
            if (status != 2 && status != 3) {
                return Result.error(400, "无效的审核状态");
            }
            
            articleService.auditArticle(articleId, status);
            String msg = status == 2 ? "审核通过" : "审核未通过";
            return Result.success(msg);
        } catch (Exception e) {
            logger.error("审核文章失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询文章列表
     */
    @GetMapping("/list")
    public Result<PageResult<Article>> getArticleList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        try {
            PageResult<Article> result = articleService.getArticleList(page, limit, userId, categoryId, status, keyword);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取推荐文章
     */
    @GetMapping("/recommend")
    public Result<List<Article>> getRecommendArticles(@RequestParam(defaultValue = "5") Integer limit) {
        try {
            List<Article> articles = articleService.getRecommendArticles(limit);
            return Result.success(articles);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取我的文章列表
     */
    @GetMapping("/my-list")
    public Result<PageResult<Article>> getMyArticles(HttpServletRequest request,
                                                   @RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer limit) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return Result.error(401, "未登录");
            }
            PageResult<Article> result = articleService.getArticleList(page, limit, userId, null, null, null);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 文章点赞
     */
    @PostMapping("/{articleId}/like")
    public Result<String> likeArticle(@PathVariable Long articleId) {
        try {
            articleService.likeArticle(articleId);
            return Result.success("点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

