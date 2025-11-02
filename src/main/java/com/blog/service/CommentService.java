package com.blog.service;

import com.blog.common.PageResult;
import com.blog.dto.CommentDTO;
import com.blog.entity.Comment;
import java.util.List;

/**
 * 评论Service接口
 */
public interface CommentService {
    
    /**
     * 创建评论
     */
    void createComment(Long userId, CommentDTO commentDTO);
    
    /**
     * 删除评论
     */
    void deleteComment(Long commentId);
    
    /**
     * 查询目标的评论列表
     */
    List<Comment> getCommentsByTarget(Integer type, Long targetId);
    
    /**
     * 分页查询评论列表
     */
    PageResult<Comment> getCommentList(Integer page, Integer limit, Integer type, Long targetId);
    
    /**
     * 评论点赞
     */
    void likeComment(Long commentId);
}


