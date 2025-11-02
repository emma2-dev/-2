package com.blog.entity;

import lombok.Data;
import java.util.Date;

/**
 * 标签实体类
 */
@Data
public class Tag {
    private Long id;
    private String name;
    private String color;
    private Date createTime;
    private Date updateTime;
}


