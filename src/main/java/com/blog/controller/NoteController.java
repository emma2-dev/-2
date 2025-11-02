package com.blog.controller;

import com.blog.common.PageResult;
import com.blog.common.Result;
import com.blog.dto.NoteDTO;
import com.blog.entity.Note;
import com.blog.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 随笔Controller
 */
@RestController
@RequestMapping("/note")
@CrossOrigin
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
    /**
     * 创建随笔
     */
    @PostMapping
    public Result<String> createNote(HttpServletRequest request, 
                                   @Validated @RequestBody NoteDTO noteDTO) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            noteService.createNote(userId, noteDTO);
            return Result.success("发布成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除随笔
     */
    @DeleteMapping("/{noteId}")
    public Result<String> deleteNote(HttpServletRequest request, @PathVariable Long noteId) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            noteService.deleteNote(userId, noteId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询随笔列表
     */
    @GetMapping("/list")
    public Result<PageResult<Note>> getNoteList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) Long userId) {
        try {
            PageResult<Note> result = noteService.getNoteList(page, limit, userId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 随笔点赞
     */
    @PostMapping("/{noteId}/like")
    public Result<String> likeNote(@PathVariable Long noteId) {
        try {
            noteService.likeNote(noteId);
            return Result.success("点赞成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

