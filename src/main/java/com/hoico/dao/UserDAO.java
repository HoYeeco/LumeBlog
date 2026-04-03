package com.hoico.dao;

import com.hoico.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDAO {
    // 根据账号查询用户
    User findByUsername(@Param("username") String username);

    // 根据用户名查询用户
    User findByNickname(@Param("nickname") String nickname);
    
    // 根据ID查询用户
    User findById(@Param("id") Integer id);
    
    // 查询所有用户
    List<User> findAll();
    
    // 新增用户
    void insert(User user);
    
    // 更新用户
    void update(User user);
}
