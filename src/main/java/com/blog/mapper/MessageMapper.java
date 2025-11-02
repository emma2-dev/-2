package com.blog.mapper;

import com.blog.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 留言Mapper
 */
@Mapper
public interface MessageMapper {
    
    /**
     * 插入留言
     */
    int insert(Message message);
    
    /**
     * 根据ID查询留言
     */
    Message selectById(@Param("id") Long id);
    
    /**
     * 删除留言
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 分页查询留言列表
     */
    List<Message> selectByPage(@Param("offset") Integer offset,
                               @Param("limit") Integer limit);
    
    /**
     * 统计留言数量
     */
    Long countMessages();
    
    /**
     * 增加点赞次数
     */
    int increaseLikeCount(@Param("id") Long id);
}


