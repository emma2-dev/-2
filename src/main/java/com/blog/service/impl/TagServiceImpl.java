package com.blog.service.impl;

import com.blog.entity.Tag;
import com.blog.mapper.TagMapper;
import com.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 标签Service实现类
 */
@Service
public class TagServiceImpl implements TagService {
    
    @Autowired
    private TagMapper tagMapper;
    
    @Override
    public void createTag(Tag tag) {
        tagMapper.insert(tag);
    }
    
    @Override
    public void updateTag(Tag tag) {
        tagMapper.update(tag);
    }
    
    @Override
    public void deleteTag(Long tagId) {
        tagMapper.deleteById(tagId);
    }
    
    @Override
    public List<Tag> getAllTags() {
        return tagMapper.selectAll();
    }
    
    @Override
    public List<Tag> getHotTags(Integer limit) {
        return tagMapper.selectHotTags(limit);
    }
}


