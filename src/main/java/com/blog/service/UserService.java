package com.blog.service;

import com.blog.common.PageResult;
import com.blog.dto.LoginDTO;
import com.blog.dto.RegisterDTO;
import com.blog.dto.UserUpdateDTO;
import com.blog.entity.User;

/**
 * 用户Service接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    String login(LoginDTO loginDTO);
    
    /**
     * 用户注册
     */
    void register(RegisterDTO registerDTO);
    
    /**
     * 获取用户信息
     */
    User getUserInfo(Long userId);
    
    /**
     * 更新用户信息
     */
    void updateUser(Long userId, UserUpdateDTO userUpdateDTO);
    
    /**
     * 修改密码
     */
    void updatePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 分页查询用户列表
     */
    PageResult<User> getUserList(Integer page, Integer limit, String keyword);
    
    /**
     * 删除用户
     */
    void deleteUser(Long userId);
}


