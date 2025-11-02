package com.blog.service;

import com.blog.common.PageResult;
import com.blog.dto.NoteDTO;
import com.blog.entity.Note;

/**
 * 随笔Service接口
 */
public interface NoteService {
    
    /**
     * 创建随笔
     */
    void createNote(Long userId, NoteDTO noteDTO);
    
    /**
     * 删除随笔
     */
    void deleteNote(Long userId, Long noteId);
    
    /**
     * 分页查询随笔列表
     */
    PageResult<Note> getNoteList(Integer page, Integer limit, Long userId);
    
    /**
     * 随笔点赞
     */
    void likeNote(Long noteId);
}


