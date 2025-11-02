package com.blog.controller;

import com.blog.common.PageResult;
import com.blog.common.Result;
import com.blog.entity.FileInfo;
import com.blog.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 文件Controller
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {
    
    @Autowired
    private FileService fileService;
    
    @Value("${file.upload-path}")
    private String uploadPath;
    
    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public Result<String> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            String url = fileService.uploadFile(userId, file);
            return Result.success("上传成功", url);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除文件
     */
    @DeleteMapping("/{fileId}")
    public Result<String> deleteFile(@PathVariable Long fileId) {
        try {
            fileService.deleteFile(fileId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询文件列表
     */
    @GetMapping("/list")
    public Result<PageResult<FileInfo>> getFileList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String category) {
        try {
            PageResult<FileInfo> result = fileService.getFileList(page, limit, userId, category);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据ID获取文件信息（用于图片显示）
     */
    @GetMapping("/image/{fileId}")
    public Result<FileInfo> getFileInfo(@PathVariable Long fileId) {
        try {
            FileInfo fileInfo = fileService.getFileById(fileId);
            if (fileInfo == null) {
                return Result.error("文件不存在");
            }
            return Result.success(fileInfo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据ID下载文件（返回实际文件内容）
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        try {
            FileInfo fileInfo = fileService.getFileById(fileId);
            if (fileInfo == null) {
                return ResponseEntity.notFound().build();
            }
            
            File file = new File(fileInfo.getFilePath());
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new FileSystemResource(file);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileInfo.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileInfo.getOriginalName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

