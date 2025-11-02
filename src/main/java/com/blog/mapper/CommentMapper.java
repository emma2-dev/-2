package com.blog.mapper;

import com.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 评论Mapper
 */
@Mapper
public interface CommentMapper {
    
    /**
     * 插入评论
     */
    int insert(Comment comment);
    
    /**
     * 根据ID查询评论
     */
    Comment selectById(@Param("id") Long id);
    
    /**
     * 删除评论
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 查询目标的评论列表（包含子评论）
     */
    List<Comment> selectByTarget(@Param("type") Integer type,
                                 @Param("targetId") Long targetId);
    
    /**
     * 分页查询评论列表
     */
    List<Comment> selectByPage(@Param("offset") Integer offset,
                               @Param("limit") Integer limit,
                               @Param("type") Integer type,
                               @Param("targetId") Long targetId);
    
    /**
     * 统计评论数量
     */
    Long countComments(@Param("type") Integer type,
                      @Param("targetId") Long targetId);
    
    /**
     * 增加点赞次数
     */
    int increaseLikeCount(@Param("id") Long id);
}


