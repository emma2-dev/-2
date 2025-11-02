package com.blog.controller;

import com.blog.common.PageResult;
import com.blog.common.Result;
import com.blog.dto.CommentDTO;
import com.blog.entity.Comment;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 评论Controller
 */
@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    /**
     * 创建评论
     */
    @PostMapping
    public Result<String> createComment(HttpServletRequest request, 
                                     @Validated @RequestBody CommentDTO commentDTO) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            commentService.createComment(userId, commentDTO);
            return Result.success("评论成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    public Result<String> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 查询目标的评论列表
     */
    @GetMapping("/target")
    public Result<List<Comment>> getCommentsByTarget(@RequestParam Integer type, 
                                                     @RequestParam Long targetId) {
        try {
            List<Comment> comments = commentService.getCommentsByTarget(type, targetId);
            return Result.success(comments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询评论列表
     */
    @GetMapping("/list")
    public Result<PageResult<Comment>> getCommentList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Long targetId) {
        try {
            PageResult<Comment> result = commentService.getCommentList(page, limit, type, targetId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 评论点赞
     */
    @PostMapping("/{commentId}/like")
    public Result<String> likeComment(@PathVariable Long commentId) {
        try {
            commentService.likeComment(commentId);
            return Result.success("点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

