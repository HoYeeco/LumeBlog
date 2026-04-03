package com.hoico.util;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoico.model.User;
import com.hoico.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 登录拦截器 - 核心特性：Spring MVC拦截器实现登录验证
 * 用于保护需要登录才能访问的API接口
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    /**
     * 前置处理 - 在请求处理之前进行拦截
     * @param request HTTP请求
     * @param response HTTP响应
     * @param handler 处理程序
     * @return 是否继续执行
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 设置允许跨域的响应头
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
        
        // 处理预检请求
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        
        // 获取请求URI和方法
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        // 获取session，同时检查user和userObj两种可能的session属性名
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("user");
        
        // 如果user不存在，尝试获取userObj
        if (userObj == null) {
            userObj = session.getAttribute("userObj");
        }
        
        // 如果session中没有用户信息，尝试从Authorization请求头获取token
        if (userObj == null) {
            String token = request.getHeader("Authorization");
            if (token != null && !token.isEmpty()) {
                // 从token中解析用户ID（简单实现，实际项目中应该使用JWT等安全机制）
                try {
                    // 解析token格式：token_timestamp_userId
                    String[] tokenParts = token.split("_");
                    if (tokenParts.length == 3) {
                        Integer userId = Integer.parseInt(tokenParts[2]);
                        // 根据用户ID从数据库中获取用户信息
                        User user = userService.getUserById(userId);
                        if (user != null) {
                            // 将用户信息存入session
                            session.setAttribute("user", user);
                            userObj = user;
                        }
                    }
                } catch (Exception e) {
                    // token解析失败，忽略
                }
            }
        }
        
        // 白名单机制：允许未登录用户访问的API
        boolean isWhiteListAPI = false;
        
        // 允许未登录用户访问文章列表和详情（GET请求）
        if (requestURI.startsWith("/api/articles") && "GET".equals(method)) {
            isWhiteListAPI = true;
        }
        
        // 允许未登录用户访问分类列表
        if (requestURI.startsWith("/api/categories") && "GET".equals(method)) {
            isWhiteListAPI = true;
        }
        
        // 允许未登录用户访问用户登录和注册接口
        if (requestURI.startsWith("/api/users/login") || requestURI.startsWith("/api/users/register")) {
            isWhiteListAPI = true;
        }
        
        // 如果是白名单API，直接放行
        if (isWhiteListAPI) {
            return true;
        }
        
        // 验证用户是否已登录（只对非白名单API进行验证）
        if (userObj == null) {
            // 设置HTTP状态码为401
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            // 创建响应结果对象
            ResponseResult result = new ResponseResult();
            result.setCode(401);
            result.setMessage("用户未登录或登录已过期");
            
            // 转换为JSON并输出
            ObjectMapper mapper = new ObjectMapper();
            out.write(mapper.writeValueAsString(result));
            out.flush();
            out.close();
            
            return false;
        }
        
        // 基于角色的权限控制
        com.hoico.model.User user = (com.hoico.model.User) userObj;
        String role = user.getRole();
        
        // 普通用户权限控制
        if ("user".equals(role) || !"admin".equals(role)) {
            // 普通用户不能访问管理员接口
            if (requestURI.contains("/api/admin/")) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                
                ResponseResult result = new ResponseResult();
                result.setCode(403);
                result.setMessage("权限不足");
                
                ObjectMapper mapper = new ObjectMapper();
                out.write(mapper.writeValueAsString(result));
                out.flush();
                out.close();
                
                return false;
            }
            

            
            // 普通用户不能管理分类（POST操作）
            if (requestURI.contains("/api/categories") && "POST".equals(method)) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                
                ResponseResult result = new ResponseResult();
                result.setCode(403);
                result.setMessage("权限不足");
                
                ObjectMapper mapper = new ObjectMapper();
                out.write(mapper.writeValueAsString(result));
                out.flush();
                out.close();
                
                return false;
            }
            
            // 普通用户对文章的PUT和DELETE操作将由ArticleController进行更精细的权限检查（作者或管理员）
            // 不再在此拦截器中阻止，让请求到达控制器进行文章所有权验证
        }
        
        // 管理员或已验证的普通用户可以继续访问
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在控制器处理完请求后执行
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在视图渲染完成后执行
    }
}