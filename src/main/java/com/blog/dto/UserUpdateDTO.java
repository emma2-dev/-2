package com.blog.dto;

import lombok.Data;
import java.util.Date;

/**
 * 用户信息更新DTO
 */
@Data
public class UserUpdateDTO {
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer gender;
    private Date birthday;
    private String introduction;
}


