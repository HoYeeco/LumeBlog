package com.hoico.service;

import com.hoico.model.Comment;

import java.util.List;

public interface CommentService {
    // 根据文章ID获取评论
    List<Comment> getCommentsByArticleId(Integer articleId);
    
    // 根据评论ID获取评论
    Comment getCommentById(Integer id);
    
    // 添加评论
    boolean addComment(Comment comment);
    
    // 删除评论
    boolean deleteComment(Integer id);
}
