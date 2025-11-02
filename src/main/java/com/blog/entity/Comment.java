package com.blog.entity;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 评论实体类
 */
@Data
public class Comment {
    private Long id;
    private Long userId;
    private Integer type;
    private Long targetId;
    private Long parentId;
    private Long replyUserId;
    private String content;
    private Integer likeCount;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    
    // 扩展字段
    private String userName;
    private String userAvatar;
    private String replyUserName;
    private List<Comment> children;
}


