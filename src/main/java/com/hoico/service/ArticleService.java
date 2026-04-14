package com.hoico.service;

import com.github.pagehelper.PageInfo;
import com.hoico.model.Article;

import java.util.List;

public interface ArticleService {
    // 获取所有文章
    PageInfo<Article> getAllArticles(int pageNum, int pageSize);
    
    // 搜索文章
    PageInfo<Article> searchArticles(int pageNum, int pageSize, String title, String author, String keyword, Integer categoryId, Integer userId, Integer currentUserId);
    
    // 根据ID获取文章
    Article getArticleById(Integer id);
    
    // 根据ID获取文章（包含当前用户的点赞状态）
    Article getArticleByIdWithLikeInfo(Integer id, Integer userId);
    
    // 根据分类获取文章
    List<Article> getArticlesByCategory(Integer categoryId);
    
    // 根据用户获取文章
    List<Article> getArticlesByUser(Integer userId);
    
    // 添加文章
    boolean addArticle(Article article);
    
    // 更新文章
    int updateArticle(Article article);
    
    // 删除文章
    int deleteArticle(Integer id);
    
    // 增加浏览量
    void increaseViewCount(Integer id);
}
