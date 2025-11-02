package com.blog.entity;

import lombok.Data;
import java.util.Date;

/**
 * 分类实体类
 */
@Data
public class Category {
    private Long id;
    private String name;
    private String description;
    private Integer sort;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}


