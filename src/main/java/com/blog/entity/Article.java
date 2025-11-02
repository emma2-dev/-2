package com.blog.entity;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 文章实体类
 */
@Data
public class Article {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private String summary;
    private String content;
    private String coverImage;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer isTop;
    private Integer isRecommend;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Date publishTime;
    
    // 扩展字段（不映射到数据库）
    private String categoryName;
    private String authorName;
    private String authorAvatar;
    private List<Tag> tags;
}


