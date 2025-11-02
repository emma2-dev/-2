package com.blog.service.impl;

import com.blog.common.PageResult;
import com.blog.entity.FileInfo;
import com.blog.mapper.FileMapper;
import com.blog.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 文件Service实现类
 */
@Service
public class FileServiceImpl implements FileService {
    
    @Autowired
    private FileMapper fileMapper;
    
    @Value("${file.upload-path}")
    private String uploadPath;
    
    @Override
    public String uploadFile(Long userId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        
        try {
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            
            // 生成新文件名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;
            
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 保存文件
            String filePath = uploadPath + fileName;
            file.transferTo(new File(filePath));
            
            // 保存文件信息到数据库
            FileInfo fileInfo = new FileInfo();
            fileInfo.setUserId(userId);
            fileInfo.setOriginalName(originalFilename);
            fileInfo.setFileName(fileName);
            fileInfo.setFilePath(filePath);
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileType(file.getContentType());
            
            // 判断文件分类
            String contentType = file.getContentType();
            if (contentType != null) {
                if (contentType.startsWith("image/")) {
                    fileInfo.setCategory("image");
                } else if (contentType.startsWith("video/")) {
                    fileInfo.setCategory("video");
                } else {
                    fileInfo.setCategory("other");
                }
            }
            
            fileMapper.insert(fileInfo);
            
            // 返回文件ID字符串，方便前端使用
            return fileInfo.getId().toString();
            
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
    
    @Override
    public void deleteFile(Long fileId) {
        FileInfo fileInfo = fileMapper.selectById(fileId);
        if (fileInfo != null) {
            // 删除物理文件
            File file = new File(fileInfo.getFilePath());
            if (file.exists()) {
                file.delete();
            }
            // 删除数据库记录
            fileMapper.deleteById(fileId);
        }
    }
    
    @Override
    public FileInfo getFileById(Long fileId) {
        return fileMapper.selectById(fileId);
    }
    
    @Override
    public PageResult<FileInfo> getFileList(Integer page, Integer limit, Long userId, String category) {
        Integer offset = (page - 1) * limit;
        List<FileInfo> files = fileMapper.selectByPage(offset, limit, userId, category);
        Long total = fileMapper.countFiles(userId, category);
        return PageResult.of(total, files);
    }
}


