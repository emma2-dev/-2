package com.blog.service.impl;

import com.blog.common.PageResult;
import com.blog.dto.CommentDTO;
import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论Service实现类
 */
@Service
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Override
    public void createComment(Long userId, CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setUserId(userId);
        comment.setLikeCount(0);
        comment.setStatus(1);
        
        if (comment.getParentId() == null) {
            comment.setParentId(0L);
        }
        
        commentMapper.insert(comment);
    }
    
    @Override
    public void deleteComment(Long commentId) {
        commentMapper.deleteById(commentId);
    }
    
    @Override
    public List<Comment> getCommentsByTarget(Integer type, Long targetId) {
        List<Comment> allComments = commentMapper.selectByTarget(type, targetId);
        
        // 构建评论树
        Map<Long, List<Comment>> childrenMap = allComments.stream()
                .filter(c -> c.getParentId() != 0)
                .collect(Collectors.groupingBy(Comment::getParentId));
        
        List<Comment> rootComments = allComments.stream()
                .filter(c -> c.getParentId() == 0)
                .collect(Collectors.toList());
        
        for (Comment comment : rootComments) {
            comment.setChildren(childrenMap.getOrDefault(comment.getId(), new ArrayList<>()));
        }
        
        return rootComments;
    }
    
    @Override
    public PageResult<Comment> getCommentList(Integer page, Integer limit, Integer type, Long targetId) {
        Integer offset = (page - 1) * limit;
        List<Comment> comments = commentMapper.selectByPage(offset, limit, type, targetId);
        Long total = commentMapper.countComments(type, targetId);
        return PageResult.of(total, comments);
    }
    
    @Override
    public void likeComment(Long commentId) {
        commentMapper.increaseLikeCount(commentId);
    }
}


