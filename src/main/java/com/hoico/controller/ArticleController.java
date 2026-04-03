package com.hoico.controller;

import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hoico.model.Article;
import com.hoico.model.User;
import com.hoico.service.ArticleService;
import com.hoico.util.ResponseResult;

/**
 * 文章控制器 - 核心特性：Spring MVC @RestController提供RESTful API
 */
@RestController // Spring MVC @RestController提供RESTful API - 核心特性之一
@RequestMapping("/api/articles")
public class ArticleController {

    private static final Logger logger = Logger.getLogger(ArticleController.class);

    // 核心特性：Spring IoC/DI自动注入Service
    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章列表（分页）
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param title 标题搜索
     * @param author 作者搜索
     * @param keyword 关键字搜索
     * @param categoryId 分类ID
     * @param userId 用户ID（用于筛选当前用户的文章）
     * @return 分页文章列表
     */
    @GetMapping
    public ResponseResult getArticles(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer userId,
            HttpSession session) {
        try {
            // 获取当前登录用户信息（如果已登录）
            User currentUser = (User) session.getAttribute("user");
            Integer currentUserId = null;
            if (currentUser != null) {
                currentUserId = currentUser.getId();
            }
            
            PageInfo<Article> pageInfo = articleService.searchArticles(pageNum, pageSize, title, author, keyword, categoryId, userId, currentUserId);
            return ResponseResult.success(pageInfo);
        } catch (Exception e) {
            return ResponseResult.error(500, "获取文章列表失败");
        }
    }

    /**
     * 获取单篇文章
     * @param id 文章ID
     * @return 文章详情
     */
    @GetMapping("/{id}")
    public ResponseResult getArticle(@PathVariable Integer id, HttpSession session) {
        try {
            Article article = articleService.getArticleById(id);
            
            if (article != null) {
                articleService.increaseViewCount(id);
                return ResponseResult.success(article);
            } else {
                return ResponseResult.error(404, "文章不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error(500, "获取文章详情失败");
        }
    }

    /**
     * 添加文章 - 核心特性：Spring声明式事务管理
     * @param article 文章信息
     * @param session HTTP会话
     * @return 操作结果
     */
    @PostMapping
    public ResponseResult addArticle(@RequestBody Article article, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            article.setUserId(user.getId());
            boolean flag = articleService.addArticle(article);
            if (flag) {
                return ResponseResult.success(article);
            } else {
                return ResponseResult.error(500, "添加文章失败");
            }
        } else {
            return ResponseResult.error(401, "未登录");
        }
    }

    /**
     * 更新文章 - 核心特性：Spring声明式事务管理
     * @param id 文章ID
     * @param article 文章信息
     * @param session HTTP会话
     * @return 操作结果
     */
    @Transactional
    @PutMapping("/{id}")
    public ResponseResult updateArticle(@PathVariable Integer id, @RequestBody Article article, HttpSession session) {
        logger.info("Updating article with id: " + id);
        User user = (User) session.getAttribute("user");
        if (user != null) {
            logger.info("Current user: " + user.getUsername() + " (id: " + user.getId() + ")");
            Article oldArticle = articleService.getArticleById(id);
            if (oldArticle != null) {
                logger.info("Original article found: " + oldArticle.getId() + " (userId: " + oldArticle.getUserId() + ")");
                // 权限检查：文章作者或管理员可以更新文章
                if (oldArticle.getUserId().equals(user.getId()) || "admin".equals(user.getRole())) {
                    article.setId(id);
                    // 更新时间
                    article.setUpdateTime(new Date());
                    logger.info("Updating article with data: " + article);
                    int rows = articleService.updateArticle(article);
                    if (rows > 0) {
                        logger.info("Article updated successfully");
                        return ResponseResult.success(article);
                    } else {
                        logger.error("Update article failed");
                        return ResponseResult.error(500, "更新文章失败");
                    }
                } else {
                    logger.info("No permission to update article");
                    return ResponseResult.error(403, "无权操作");
                }
            } else {
                logger.info("Article not found: " + id);
                return ResponseResult.error(404, "文章不存在");
            }
        } else {
            logger.info("User not logged in");
            return ResponseResult.error(401, "未登录");
        }
    }

    /**
     * 删除文章 - 核心特性：Spring声明式事务管理
     * @param id 文章ID
     * @param session HTTP会话
     * @return 操作结果
     */
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Integer id, HttpSession session) {
        logger.info("Deleting article with id: " + id);
        User user = (User) session.getAttribute("user");
        if (user != null) {
            logger.info("Current user: " + user.getUsername() + " (id: " + user.getId() + ")");
            Article article = articleService.getArticleById(id);
            if (article != null) {
                logger.info("Article found: " + article.getId() + " (userId: " + article.getUserId() + ")");
                // 权限检查：文章作者或管理员可以删除文章
                if (article.getUserId().equals(user.getId()) || "admin".equals(user.getRole())) {
                    logger.info("Deleting article: " + id);
                    int rows = articleService.deleteArticle(id);
                    if (rows > 0) {
                        logger.info("Article deleted successfully");
                        return ResponseResult.success();
                    } else {
                        logger.error("Delete article failed");
                        return ResponseResult.error(500, "删除文章失败");
                    }
                } else {
                    logger.info("No permission to delete article");
                    return ResponseResult.error(403, "无权操作");
                }
            } else {
                logger.info("Article not found: " + id);
                return ResponseResult.error(404, "文章不存在");
            }
        } else {
            logger.info("User not logged in");
            return ResponseResult.error(401, "未登录");
        }
    }
    
    /**
     * 获取相关文章（同一分类下的其他文章）
     * @param categoryId 分类ID
     * @param excludeId 排除的文章ID
     * @return 相关文章列表
     */
    @GetMapping("/related")
    public ResponseResult getRelatedArticles(@RequestParam Integer categoryId, @RequestParam Integer excludeId) {
        try {
            // 简单实现：获取同一分类下的其他文章，最多返回5篇
            List<Article> relatedArticles = articleService.searchArticles(1, 5, null, null, null, categoryId, null, null).getList()
                .stream()
                .filter(article -> !article.getId().equals(excludeId))
                .limit(5)
                .collect(java.util.stream.Collectors.toList());
            return ResponseResult.success(relatedArticles);
        } catch (Exception e) {
            logger.error("获取相关文章失败", e);
            return ResponseResult.error(500, "获取相关文章失败");
        }
    }
}