package com.blog.service;

import com.blog.common.PageResult;
import com.blog.entity.Message;

/**
 * 留言Service接口
 */
public interface MessageService {
    
    /**
     * 创建留言
     */
    void createMessage(Long userId, String content);
    
    /**
     * 删除留言
     */
    void deleteMessage(Long messageId);
    
    /**
     * 分页查询留言列表
     */
    PageResult<Message> getMessageList(Integer page, Integer limit);
    
    /**
     * 留言点赞
     */
    void likeMessage(Long messageId);
}


