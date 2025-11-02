package com.blog.service.impl;

import com.blog.common.PageResult;
import com.blog.dto.NoteDTO;
import com.blog.entity.Note;
import com.blog.mapper.NoteMapper;
import com.blog.service.NoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 随笔Service实现类
 */
@Service
public class NoteServiceImpl implements NoteService {
    
    @Autowired
    private NoteMapper noteMapper;
    
    @Override
    public void createNote(Long userId, NoteDTO noteDTO) {
        Note note = new Note();
        BeanUtils.copyProperties(noteDTO, note);
        note.setUserId(userId);
        note.setLikeCount(0);
        note.setCommentCount(0);
        note.setStatus(1);
        noteMapper.insert(note);
    }
    
    @Override
    public void deleteNote(Long userId, Long noteId) {
        Note note = noteMapper.selectById(noteId);
        if (note == null) {
            throw new RuntimeException("随笔不存在");
        }
        
        // 验证权限
        if (!note.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }
        
        noteMapper.deleteById(noteId);
    }
    
    @Override
    public PageResult<Note> getNoteList(Integer page, Integer limit, Long userId) {
        Integer offset = (page - 1) * limit;
        List<Note> notes = noteMapper.selectByPage(offset, limit, userId);
        Long total = noteMapper.countNotes(userId);
        return PageResult.of(total, notes);
    }
    
    @Override
    public void likeNote(Long noteId) {
        noteMapper.increaseLikeCount(noteId);
    }
}


