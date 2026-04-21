package com.hoico.dao;

import com.hoico.model.Like;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 点赞DAO接口
 */
public interface LikeDAO {
    // 根据文章ID查询点赞列表
    List<Like> findByArticleId(@Param("articleId") Integer articleId);
    
    // 根据用户ID查询点赞列表
    List<Like> findByUserId(@Param("userId") Integer userId);
    
    // 检查用户是否已点赞文章
    Like findByArticleAndUser(@Param("articleId") Integer articleId, @Param("userId") Integer userId);
    
    // 新增点赞
    void insert(Like like);
    
    // 删除点赞
    void delete(@Param("id") Integer id);
    
    // 根据文章ID和用户ID删除点赞
    void deleteByArticleAndUser(@Param("articleId") Integer articleId, @Param("userId") Integer userId);
    
    // 获取文章的点赞数
    Integer countByArticleId(@Param("articleId") Integer articleId);
}
