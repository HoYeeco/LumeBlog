package com.hoico.model;

import java.util.Date;

/**
 * 点赞实体类
 * 表示用户对文章的点赞关系
 */
public class Like {
    private Integer id;
    private Integer articleId; // 文章ID
    private Integer userId;    // 用户ID
    private Date createTime;   // 点赞时间
    
    // 关联属性
    private Article article;
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
