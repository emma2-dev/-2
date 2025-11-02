package com.blog.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Carousel {
    private Long id;
    private String imgUrl;
    private String targetUrl;
    private String title;
    private Integer sort;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}

