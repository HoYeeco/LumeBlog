package com.hoico.service.impl;

import com.hoico.dao.UserDAO;
import com.hoico.model.User;
import com.hoico.service.UserService;
import com.hoico.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service // Spring IoC注解 - 自动将该类注册为Spring容器中的Bean
public class UserServiceImpl implements UserService {

    @Autowired // Spring DI注解 - 自动注入UserDAO依赖
    private UserDAO userDAO;

    @Override
    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        // 直接比较密码，因为Controller层已经进行了MD5加密
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (userDAO.findByUsername(user.getUsername()) != null) {
            return false;
        }
        // 密码加密
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        // 设置创建和更新时间
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userDAO.insert(user);
        return true;
    }

    @Override
    public User getUserById(Integer id) {
        return userDAO.findById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public User getUserByNickname(String nickname) {return userDAO.findByNickname(nickname);}

    @Override
    public List<User> findAllUsers() {
        return userDAO.findAll();
    }
}
