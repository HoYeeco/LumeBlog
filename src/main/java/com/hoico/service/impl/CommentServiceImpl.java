package com.hoico.service.impl;

import com.hoico.dao.CommentDAO;
import com.hoico.model.Comment;
import com.hoico.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service // Spring IoC注解
public class CommentServiceImpl implements CommentService {

    @Autowired // Spring DI注解
    private CommentDAO commentDAO;

    @Override
    public List<Comment> getCommentsByArticleId(Integer articleId) {
        return commentDAO.findByArticleId(articleId);
    }

    @Override
    @Transactional // Spring声明式事务 - 用于评论发布操作
    public boolean addComment(Comment comment) {
        comment.setCreateTime(new Date());
        commentDAO.insert(comment);
        return true;
    }

    @Override
    public Comment getCommentById(Integer id) {
        return commentDAO.findById(id);
    }

    @Override
    @Transactional // Spring声明式事务
    public boolean deleteComment(Integer id) {
        commentDAO.delete(id);
        return true;
    }
}
