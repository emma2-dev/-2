package com.blog.service.impl;

import com.blog.common.PageResult;
import com.blog.dto.LoginDTO;
import com.blog.dto.RegisterDTO;
import com.blog.dto.UserUpdateDTO;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import com.blog.util.JwtUtil;
import com.blog.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public String login(LoginDTO loginDTO) {
        // 根据用户名查询用户
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证密码
        if (!PasswordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        
        // 生成Token
        return jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
    }
    
    @Override
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否存在
        User existUser = userMapper.selectByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(PasswordUtil.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname());
        user.setEmail(registerDTO.getEmail());
        user.setRole("USER");
        user.setStatus(1);
        
        userMapper.insert(user);
    }
    
    @Override
    public User getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 清空密码
        user.setPassword(null);
        return user;
    }
    
    @Override
    public void updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        BeanUtils.copyProperties(userUpdateDTO, user);
        user.setId(userId);
        userMapper.update(user);
    }
    
    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证原密码
        if (!PasswordUtil.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        // 更新密码
        user.setPassword(PasswordUtil.encode(newPassword));
        userMapper.update(user);
    }
    
    @Override
    public PageResult<User> getUserList(Integer page, Integer limit, String keyword) {
        Integer offset = (page - 1) * limit;
        List<User> users = userMapper.selectByPage(offset, limit, keyword);
        Long total = userMapper.countUsers(keyword);
        
        // 清空密码
        users.forEach(user -> user.setPassword(null));
        
        return PageResult.of(total, users);
    }
    
    @Override
    public void deleteUser(Long userId) {
        userMapper.deleteById(userId);
    }
}


