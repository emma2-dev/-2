package com.blog.entity;

import lombok.Data;
import java.util.Date;

/**
 * 留言实体类
 */
@Data
public class Message {
    private Long id;
    private Long userId;
    private String content;
    private Integer likeCount;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    
    // 扩展字段
    private String userName;
    private String userAvatar;
}


