package com.blog.controller;

import com.blog.common.PageResult;
import com.blog.common.Result;
import com.blog.dto.UserUpdateDTO;
import com.blog.entity.User;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户Controller
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            User user = userService.getUserInfo(userId);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<String> updateUser(HttpServletRequest request, @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            userService.updateUser(userId, userUpdateDTO);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<String> updatePassword(HttpServletRequest request, 
                                      @RequestParam String oldPassword,
                                      @RequestParam String newPassword) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            userService.updatePassword(userId, oldPassword, newPassword);
            return Result.success("密码修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询用户列表（管理员）
     */
    @GetMapping("/list")
    public Result<PageResult<User>> getUserList(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer limit,
                                                @RequestParam(required = false) String keyword) {
        try {
            PageResult<User> result = userService.getUserList(page, limit, keyword);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除用户（管理员）
     */
    @DeleteMapping("/{userId}")
    public Result<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

