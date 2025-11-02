package com.blog.mapper;

import com.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper {
    
    /**
     * 插入用户
     */
    int insert(User user);
    
    /**
     * 根据ID查询用户
     */
    User selectById(@Param("id") Long id);
    
    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);
    
    /**
     * 更新用户信息
     */
    int update(User user);
    
    /**
     * 删除用户
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 分页查询用户列表
     */
    List<User> selectByPage(@Param("offset") Integer offset, 
                            @Param("limit") Integer limit,
                            @Param("keyword") String keyword);
    
    /**
     * 统计用户数量
     */
    Long countUsers(@Param("keyword") String keyword);
}


