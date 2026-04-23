package com.hoico.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hoico.dao.ArticleDAO;
import com.hoico.model.Article;
import com.hoico.service.ArticleService;
import com.hoico.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service // Spring IoC注解
@Transactional // Spring声明式事务 - 用于文章发布和修改等操作
public class ArticleServiceImpl implements ArticleService {

    @Autowired // Spring DI注解
    private ArticleDAO articleDAO;
    
    @Autowired
    private LikeService likeService;

    @Override
    public PageInfo<Article> getAllArticles(int pageNum, int pageSize) {
        // MyBatis PageHelper插件实现分页 - 核心特性之一
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleDAO.findAll();
        // 为每个文章设置点赞数和当前用户的点赞状态
        // 注意：这里没有用户ID，所以无法判断是否点赞，默认设置为false
        for (Article article : articles) {
            article.setLikeCount(likeService.getLikeCountByArticleId(article.getId()));
            article.setIsLiked(false);
        }
        return new PageInfo<>(articles);
    }

    @Override
    public PageInfo<Article> searchArticles(int pageNum, int pageSize, String title, String author, String keyword, Integer categoryId, Integer userId, Integer currentUserId) {
        // MyBatis PageHelper插件实现分页 - 核心特性之一
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleDAO.searchArticles(title, author, keyword, categoryId, userId);
        // 为每个文章设置点赞数和当前用户的点赞状态
        for (Article article : articles) {
            article.setLikeCount(likeService.getLikeCountByArticleId(article.getId()));
            // 如果提供了currentUserId，设置当前用户的点赞状态
            if (currentUserId != null) {
                article.setIsLiked(likeService.isLiked(article.getId(), currentUserId));
            } else {
                // 如果没有提供currentUserId（比如首页列表），默认设置为false
                article.setIsLiked(false);
            }
        }
        return new PageInfo<>(articles);
    }

    @Override
    public Article getArticleById(Integer id) {
        Article article = articleDAO.findById(id);
        if (article != null) {
            // 设置点赞数
            article.setLikeCount(likeService.getLikeCountByArticleId(id));
        }
        return article;
    }

    @Override
    public Article getArticleByIdWithLikeInfo(Integer id, Integer userId) {
        Article article = articleDAO.findById(id);
        if (article != null) {
            // 设置点赞数
            article.setLikeCount(likeService.getLikeCountByArticleId(id));
            // 设置当前用户的点赞状态
            if (userId != null) {
                article.setIsLiked(likeService.isLiked(id, userId));
            } else {
                article.setIsLiked(false);
            }
        }
        return article;
    }

    @Override
    public List<Article> getArticlesByCategory(Integer categoryId) {
        List<Article> articles = articleDAO.findByCategory(categoryId);
        // 为每个文章设置点赞数
        for (Article article : articles) {
            article.setLikeCount(likeService.getLikeCountByArticleId(article.getId()));
            article.setIsLiked(false); // 默认设置为未点赞，因为没有用户ID
        }
        return articles;
    }

    @Override
    public List<Article> getArticlesByUser(Integer userId) {
        List<Article> articles = articleDAO.findByUser(userId);
        // 为每个文章设置点赞数和当前用户的点赞状态
        for (Article article : articles) {
            article.setLikeCount(likeService.getLikeCountByArticleId(article.getId()));
            // 这里的userId是当前用户的ID，所以可以判断是否点赞
            article.setIsLiked(likeService.isLiked(article.getId(), userId));
        }
        return articles;
    }

    @Override
    @Transactional // Spring声明式事务 - 核心特性之一
    public boolean addArticle(Article article) {
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        article.setViewCount(0);
        articleDAO.insert(article);
        return true;
    }

    @Override
    @Transactional // Spring声明式事务
    public int updateArticle(Article article) {
        article.setUpdateTime(new Date());
        return articleDAO.update(article);
    }

    @Override
    @Transactional // Spring声明式事务
    public int deleteArticle(Integer id) {
        return articleDAO.delete(id);
    }

    @Override
    public void increaseViewCount(Integer id) {
        articleDAO.increaseViewCount(id);
    }
}
