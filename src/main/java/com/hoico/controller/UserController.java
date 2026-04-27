package com.hoico.controller;

import com.hoico.model.User;
import com.hoico.service.UserService;
import java.util.Date;
import java.util.Map;
import com.hoico.util.MD5Util;
import com.hoico.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

/**
 * 用户控制器 - 提供用户认证和管理功能
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    // 核心特性：Spring IoC/DI自动注入Service
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param username 账号
     * @param password 密码
     * @param session HTTP会话
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody Map<String, String> loginData, HttpSession session) {
        // 获取用户名和密码
        String username = loginData.get("username");
        String password = loginData.get("password");
        // 核心特性：密码加密验证
        String encryptedPassword = MD5Util.encrypt(password);
        User user = userService.login(username, encryptedPassword);
        if (user != null) {
            // 将用户信息存入session，用于Spring MVC拦截器验证
            session.setAttribute("user", user);
            // 生成token用于前端存储
            String token = "token_" + System.currentTimeMillis() + "_" + user.getId();
            session.setAttribute("token", token);
            // 登录成功，返回用户信息（不包含密码）和token
            user.setPassword(null);
            return ResponseResult.success(new LoginResponse(user, token));
        } else {
            return ResponseResult.error("用户名或密码错误");
        }
    }

    /**
     * 用户注册 - 核心特性：Spring声明式事务管理
     * @param user 用户信息
     * @return 注册结果
     */
    @Transactional
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        // 检查用户名是否已存在
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return ResponseResult.error("账号已存在");
        }

        // 设置创建时间和更新时间
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        
        // 设置默认角色为普通用户
        user.setRole("user");
        
        // 保存用户
        boolean flag = userService.register(user);
        return ResponseResult.success();
    }

    /**
     * 用户登出
     * @param session HTTP会话
     * @return 登出结果
     */
    @PostMapping("/logout")
    public ResponseResult logout(HttpSession session) {
        session.invalidate();
        return ResponseResult.success();
    }

    /**
     * 获取当前登录用户信息
     * @param session HTTP会话
     * @return 用户信息
     */
    @GetMapping("/current")
    public ResponseResult getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            user.setPassword(null);
            return ResponseResult.success(user);
        } else {
            return ResponseResult.error("用户未登录");
        }
    }

    /**
     * 登录响应内部类
     */
    private static class LoginResponse {
        private User user;
        private String token;

        public LoginResponse(User user, String token) {
            this.user = user;
            this.token = token;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
