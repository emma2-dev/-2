package com.blog.controller;

import com.blog.common.PageResult;
import com.blog.common.Result;
import com.blog.entity.Message;
import com.blog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 留言Controller
 */
@RestController
@RequestMapping("/message")
@CrossOrigin
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    /**
     * 创建留言
     */
    @PostMapping
    public Result<String> createMessage(HttpServletRequest request, @RequestParam String content) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            messageService.createMessage(userId, content);
            return Result.success("留言成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除留言
     */
    @DeleteMapping("/{messageId}")
    public Result<String> deleteMessage(@PathVariable Long messageId) {
        try {
            messageService.deleteMessage(messageId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询留言列表
     */
    @GetMapping("/list")
    public Result<PageResult<Message>> getMessageList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            PageResult<Message> result = messageService.getMessageList(page, limit);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 留言点赞
     */
    @PostMapping("/{messageId}/like")
    public Result<String> likeMessage(@PathVariable Long messageId) {
        try {
            messageService.likeMessage(messageId);
            return Result.success("点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

