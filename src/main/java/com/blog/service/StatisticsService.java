package com.blog.service;

import java.util.Map;

/**
 * 统计Service接口
 */
public interface StatisticsService {
    
    /**
     * 获取系统统计数据
     */
    Map<String, Long> getStatistics();
}

