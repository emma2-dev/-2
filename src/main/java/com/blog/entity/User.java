package com.blog.entity;

import lombok.Data;
import java.util.Date;

/**
 * 用户实体类
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer gender;
    private Date birthday;
    private String introduction;
    private String role;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}


