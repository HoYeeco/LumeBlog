package com.hoico.controller;

import com.hoico.model.User;
import com.hoico.service.LikeService;
import com.hoico.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 点赞控制器
 */
@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    /**
     * 切换点赞状态
     */
    @PostMapping("/toggle")
    @ResponseBody
    public ResponseResult toggleLike(@RequestBody Map<String, Integer> request, HttpSession session) {
        // 获取文章ID
        Integer articleId = request.get("articleId");
        if (articleId == null) {
            return ResponseResult.error(400, "文章ID不能为空");
        }
        
        // 检查用户是否登录
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseResult.error(401, "请先登录");
        }
        
        try {
            // 切换点赞状态
            likeService.toggleLike(articleId, user.getId());
            // 获取最新的点赞状态和数量
            boolean isLiked = likeService.isLiked(articleId, user.getId());
            int likeCount = likeService.getLikeCountByArticleId(articleId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("isLiked", isLiked); // true表示当前是点赞状态，false表示当前是未点赞状态
            data.put("likeCount", likeCount);
            
            return ResponseResult.success(data);
        } catch (Exception e) {
            return ResponseResult.error(500, "操作失败：" + e.getMessage());
        }
    }

    /**
     * 获取文章的点赞数
     */
    @GetMapping("/count/{articleId}")
    @ResponseBody
    public ResponseResult getLikeCount(@PathVariable Integer articleId) {
        try {
            int likeCount = likeService.getLikeCountByArticleId(articleId);
            return ResponseResult.success(likeCount);
        } catch (Exception e) {
            return ResponseResult.error(500, "获取点赞数失败：" + e.getMessage());
        }
    }

    /**
     * 检查用户是否已点赞文章
     */
    @GetMapping("/check/{articleId}")
    @ResponseBody
    public ResponseResult checkLike(@PathVariable Integer articleId, HttpSession session) {
        // 检查用户是否登录
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseResult.success(false); // 未登录用户视为未点赞
        }
        
        try {
            boolean isLike = likeService.isLiked(articleId, user.getId());
            return ResponseResult.success(isLike);
        } catch (Exception e) {
            return ResponseResult.error(500, "检查点赞状态失败：" + e.getMessage());
        }
    }
}
