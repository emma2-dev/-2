package com.blog.mapper;

import com.blog.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 文件Mapper
 */
@Mapper
public interface FileMapper {
    
    /**
     * 插入文件信息
     */
    int insert(FileInfo fileInfo);
    
    /**
     * 根据ID查询文件信息
     */
    FileInfo selectById(@Param("id") Long id);
    
    /**
     * 删除文件信息
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 分页查询文件列表
     */
    List<FileInfo> selectByPage(@Param("offset") Integer offset,
                                @Param("limit") Integer limit,
                                @Param("userId") Long userId,
                                @Param("category") String category);
    
    /**
     * 统计文件数量
     */
    Long countFiles(@Param("userId") Long userId,
                    @Param("category") String category);
}


