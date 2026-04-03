package com.hoico.service;

import com.hoico.model.User;
import java.util.List;

public interface UserService {
    // 用户登录
    User login(String username, String password);
    
    // 用户注册
    boolean register(User user);
    
    // 根据ID查询用户
    User getUserById(Integer id);
    
    // 根据账号查询用户
    User getUserByUsername(String username);

    // 根据用户名查询用户
    User getUserByNickname(String nickname);
    
    // 查询所有用户
    List<User> findAllUsers();
}
