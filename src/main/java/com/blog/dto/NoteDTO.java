package com.blog.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 随笔DTO
 */
@Data
public class NoteDTO {
    @NotBlank(message = "随笔内容不能为空")
    private String content;
    
    private String images;
}


