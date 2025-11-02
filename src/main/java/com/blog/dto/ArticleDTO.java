package com.blog.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 文章DTO
 */
@Data
public class ArticleDTO {
    private Long id;
    
    private Long categoryId;
    
    @NotBlank(message = "文章标题不能为空")
    private String title;
    
    private String summary;
    
    @NotBlank(message = "文章内容不能为空")
    private String content;
    
    private String coverImage;
    
    private Integer isTop;
    
    private Integer isRecommend;
    
    private Integer status;
    
    private List<Long> tagIds;
}


