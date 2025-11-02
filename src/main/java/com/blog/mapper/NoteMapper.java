package com.blog.mapper;

import com.blog.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 随笔Mapper
 */
@Mapper
public interface NoteMapper {
    
    /**
     * 插入随笔
     */
    int insert(Note note);
    
    /**
     * 根据ID查询随笔
     */
    Note selectById(@Param("id") Long id);
    
    /**
     * 更新随笔
     */
    int update(Note note);
    
    /**
     * 删除随笔
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 分页查询随笔列表
     */
    List<Note> selectByPage(@Param("offset") Integer offset,
                            @Param("limit") Integer limit,
                            @Param("userId") Long userId);
    
    /**
     * 统计随笔数量
     */
    Long countNotes(@Param("userId") Long userId);
    
    /**
     * 增加点赞次数
     */
    int increaseLikeCount(@Param("id") Long id);
}


