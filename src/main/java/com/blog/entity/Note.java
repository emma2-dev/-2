package com.blog.entity;

import lombok.Data;
import java.util.Date;

/**
 * 随笔实体类
 */
@Data
public class Note {
    private Long id;
    private Long userId;
    private String content;
    private String images;
    private Integer likeCount;
    private Integer commentCount;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    
    // 扩展字段
    private String authorName;
    private String authorAvatar;
}


