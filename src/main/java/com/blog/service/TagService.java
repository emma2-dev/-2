package com.blog.service;

import com.blog.entity.Tag;
import java.util.List;

/**
 * 标签Service接口
 */
public interface TagService {
    
    /**
     * 创建标签
     */
    void createTag(Tag tag);
    
    /**
     * 更新标签
     */
    void updateTag(Tag tag);
    
    /**
     * 删除标签
     */
    void deleteTag(Long tagId);
    
    /**
     * 获取所有标签
     */
    List<Tag> getAllTags();
    
    /**
     * 获取热门标签
     */
    List<Tag> getHotTags(Integer limit);
}


