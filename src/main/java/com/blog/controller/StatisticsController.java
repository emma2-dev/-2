package com.blog.controller;

import com.blog.common.Result;
import com.blog.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 统计Controller
 */
@RestController
@RequestMapping("/statistics")
@CrossOrigin
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    /**
     * 获取系统统计数据
     */
    @GetMapping
    public Result<Map<String, Long>> getStatistics() {
        Map<String, Long> stats = statisticsService.getStatistics();
        return Result.success(stats);
    }
}

