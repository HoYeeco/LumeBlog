package com.hoico.dao;

import com.hoico.model.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentDAO {
    // 根据文章ID查询评论
    List<Comment> findByArticleId(@Param("articleId") Integer articleId);
    
    // 根据用户ID查询评论
    List<Comment> findByUserId(@Param("userId") Integer userId);
    
    // 根据ID查询评论
    Comment findById(@Param("id") Integer id);
    
    // 新增评论
    void insert(Comment comment);
    
    // 删除评论
    void delete(@Param("id") Integer id);
}
