package com.blog.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 评论DTO
 */
@Data
public class CommentDTO {
    @NotNull(message = "评论类型不能为空")
    private Integer type;
    
    @NotNull(message = "目标ID不能为空")
    private Long targetId;
    
    private Long parentId;
    
    private Long replyUserId;
    
    @NotBlank(message = "评论内容不能为空")
    private String content;
}


