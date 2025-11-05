package com.blog.service.impl;

import com.blog.mapper.*;
import com.blog.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计Service实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    
    @Autowired
    private ArticleMapper articleMapper;
    
    @Autowired
    private NoteMapper noteMapper;
    
    @Autowired
    private MessageMapper messageMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public Map<String, Long> getStatistics() {
        Map<String, Long> stats = new HashMap<>();
        
        // 统计文章总数（所有状态）
        Long articleCount = articleMapper.countArticles(null, null, null, null);
        stats.put("articleCount", articleCount != null ? articleCount : 0L);
        
        // 统计随笔总数
        Long noteCount = noteMapper.countNotes(null);
        stats.put("noteCount", noteCount != null ? noteCount : 0L);
        
        // 统计留言总数
        Long messageCount = messageMapper.countMessages();
        stats.put("messageCount", messageCount != null ? messageCount : 0L);
        
        // 统计用户总数
        Long userCount = userMapper.countUsers(null);
        stats.put("userCount", userCount != null ? userCount : 0L);
        
        return stats;
    }
}






