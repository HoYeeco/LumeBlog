package com.hoico.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hoico.dao.ArticleDAO;
import com.hoico.model.Article;
import com.hoico.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service // Spring IoC注解
@Transactional // Spring声明式事务 - 用于文章发布和修改等操作
public class ArticleServiceImpl implements ArticleService {

    @Autowired // Spring DI注解@Autowired
    private ArticleDAO articleDAO;

    @Override
    public PageInfo<Article> getAllArticles(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleDAO.findAll();
        return new PageInfo<>(articles);
    }

    @Override
    public PageInfo<Article> searchArticles(int pageNum, int pageSize, String title, String author, String keyword, Integer categoryId, Integer userId, Integer currentUserId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleDAO.searchArticles(title, author, keyword, categoryId, userId);
        return new PageInfo<>(articles);
    }

    @Override
    public Article getArticleById(Integer id) {
        return articleDAO.findById(id);
    }

    @Override
    public List<Article> getArticlesByCategory(Integer categoryId) {
        return articleDAO.findByCategory(categoryId);
    }

    @Override
    public List<Article> getArticlesByUser(Integer userId) {
        return articleDAO.findByUser(userId);
    }

    @Override
    @Transactional
    public boolean addArticle(Article article) {
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        article.setViewCount(0);
        articleDAO.insert(article);
        return true;
    }

    @Override
    @Transactional
    public int updateArticle(Article article) {
        article.setUpdateTime(new Date());
        return articleDAO.update(article);
    }

    @Override
    @Transactional
    public int deleteArticle(Integer id) {
        return articleDAO.delete(id);
    }

    @Override
    public void increaseViewCount(Integer id) {
        articleDAO.increaseViewCount(id);
    }
}

