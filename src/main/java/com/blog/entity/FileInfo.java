package com.blog.entity;

import lombok.Data;
import java.util.Date;

/**
 * 文件信息实体类
 */
@Data
public class FileInfo {
    private Long id;
    private Long userId;
    private String originalName;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private String category;
    private Date createTime;
}


