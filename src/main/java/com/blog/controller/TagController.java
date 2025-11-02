package com.blog.controller;

import com.blog.common.Result;
import com.blog.entity.Tag;
import com.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签Controller
 */
@RestController
@RequestMapping("/tag")
@CrossOrigin
public class TagController {
    
    @Autowired
    private TagService tagService;
    
    /**
     * 创建标签
     */
    @PostMapping
    public Result<String> createTag(@RequestBody Tag tag) {
        try {
            tagService.createTag(tag);
            return Result.success("创建成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新标签
     */
    @PutMapping
    public Result<String> updateTag(@RequestBody Tag tag) {
        try {
            tagService.updateTag(tag);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除标签
     */
    @DeleteMapping("/{tagId}")
    public Result<String> deleteTag(@PathVariable Long tagId) {
        try {
            tagService.deleteTag(tagId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取所有标签
     */
    @GetMapping("/list")
    public Result<List<Tag>> getAllTags() {
        try {
            List<Tag> tags = tagService.getAllTags();
            return Result.success(tags);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取热门标签
     */
    @GetMapping("/hot")
    public Result<List<Tag>> getHotTags(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Tag> tags = tagService.getHotTags(limit);
            return Result.success(tags);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

