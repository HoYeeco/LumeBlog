package com.hoico.dao;

import com.hoico.model.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleDAO {
    // 查询所有文章
    List<Article> findAll();
    
    // 搜索文章
    List<Article> searchArticles(@Param("title") String title, @Param("author") String author, @Param("keyword") String keyword, @Param("categoryId") Integer categoryId, @Param("userId") Integer userId);
    
    // 根据ID查询文章
    Article findById(@Param("id") Integer id);
    
    // 根据分类查询文章
    List<Article> findByCategory(@Param("categoryId") Integer categoryId);
    
    // 根据用户ID查询文章
    List<Article> findByUser(@Param("userId") Integer userId);
    
    // 新增文章
    void insert(Article article);
    
    // 更新文章
    int update(Article article);
    
    // 删除文章
    int delete(@Param("id") Integer id);
    
    // 增加浏览量
    void increaseViewCount(@Param("id") Integer id);
}