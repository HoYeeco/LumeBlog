package com.hoico.controller;

import com.hoico.model.Comment;
import com.hoico.model.User;
import com.hoico.service.CommentService;
import java.util.Date;
import com.hoico.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * 评论控制器 - 提供文章评论功能
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    // 核心特性：Spring IoC/DI自动注入Service
    @Autowired
    private CommentService commentService;

    /**
     * 获取文章的评论列表
     * @param articleId 文章ID
     * @return 评论列表
     */
    @GetMapping("/article/{articleId}")
    public ResponseResult list(@PathVariable Long articleId) {
        List<Comment> comments = commentService.getCommentsByArticleId(articleId.intValue());
        return ResponseResult.success(comments);
    }

    /**
     * 添加评论 - 核心特性：Spring声明式事务管理
     * @param comment 评论信息
     * @param session HTTP会话
     * @return 操作结果
     */
    @Transactional
    @PostMapping
    public ResponseResult add(@RequestBody Comment comment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseResult.error(401, "用户未登录");
        }
        
        // 设置评论用户和时间
        comment.setUserId(user.getId());
        comment.setCreateTime(new Date());
        
        // 保存评论
        boolean flag = commentService.addComment(comment);
        if (flag) {
            return ResponseResult.success(comment);
        } else {
            return ResponseResult.error(500, "评论失败");
        }
    }

    /**
     * 删除评论 - 核心特性：Spring声明式事务管理
     * @param id 评论ID
     * @param session HTTP会话
     * @return 操作结果
     */
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseResult.error(401, "用户未登录");
        }
        
        // 查询评论确认作者身份
        Comment comment = commentService.getCommentById(id.intValue());
        if (comment == null) {
            return ResponseResult.error(404, "评论不存在");
        }
        
        // 权限验证：评论作者或管理员可删除评论
        if (!comment.getUserId().equals(user.getId()) && !"admin".equals(user.getRole())) {
            return ResponseResult.error(403, "无权限删除此评论");
        }
        
        boolean flag = commentService.deleteComment(id.intValue());
        if (flag) {
            return ResponseResult.success();
        } else {
            return ResponseResult.error(500, "删除失败");
        }
    }
}
