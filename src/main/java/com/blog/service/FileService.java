package com.blog.service;

import com.blog.common.PageResult;
import com.blog.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件Service接口
 */
public interface FileService {
    
    /**
     * 上传文件
     */
    String uploadFile(Long userId, MultipartFile file);
    
    /**
     * 删除文件
     */
    void deleteFile(Long fileId);
    
    /**
     * 根据ID查询文件信息
     */
    FileInfo getFileById(Long fileId);
    
    /**
     * 分页查询文件列表
     */
    PageResult<FileInfo> getFileList(Integer page, Integer limit, Long userId, String category);
}


