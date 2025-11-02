package com.blog.service.impl;

import com.blog.common.PageResult;
import com.blog.entity.Message;
import com.blog.mapper.MessageMapper;
import com.blog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 留言Service实现类
 */
@Service
public class MessageServiceImpl implements MessageService {
    
    @Autowired
    private MessageMapper messageMapper;
    
    @Override
    public void createMessage(Long userId, String content) {
        Message message = new Message();
        message.setUserId(userId);
        message.setContent(content);
        message.setLikeCount(0);
        message.setStatus(1);
        messageMapper.insert(message);
    }
    
    @Override
    public void deleteMessage(Long messageId) {
        messageMapper.deleteById(messageId);
    }
    
    @Override
    public PageResult<Message> getMessageList(Integer page, Integer limit) {
        Integer offset = (page - 1) * limit;
        List<Message> messages = messageMapper.selectByPage(offset, limit);
        Long total = messageMapper.countMessages();
        return PageResult.of(total, messages);
    }
    
    @Override
    public void likeMessage(Long messageId) {
        messageMapper.increaseLikeCount(messageId);
    }
}


