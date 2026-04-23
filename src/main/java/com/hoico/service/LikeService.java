package com.hoico.service;

import com.hoico.model.Like;

import java.util.List;

/**
 * 点赞服务接口
 */
public interface LikeService {
    // 根据文章ID获取点赞列表
    List<Like> getLikesByArticleId(Integer articleId);
    
    // 根据用户ID获取点赞列表
    List<Like> getLikesByUserId(Integer userId);
    
    // 检查用户是否已点赞文章
    boolean isLiked(Integer articleId, Integer userId);
    
    // 获取文章的点赞数
    Integer getLikeCountByArticleId(Integer articleId);
    
    // 添加点赞
    boolean addLike(Integer articleId, Integer userId);
    
    // 取消点赞
    boolean cancelLike(Integer articleId, Integer userId);
    
    // 切换点赞状态（已点赞则取消，未点赞则添加）
    boolean toggleLike(Integer articleId, Integer userId);
}
