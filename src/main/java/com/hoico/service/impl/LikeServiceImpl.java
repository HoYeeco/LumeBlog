package com.hoico.service.impl;

import com.hoico.dao.LikeDAO;
import com.hoico.model.Like;
import com.hoico.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 点赞服务实现类
 */
@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeDAO likeDAO;

    @Override
    public List<Like> getLikesByArticleId(Integer articleId) {
        return likeDAO.findByArticleId(articleId);
    }

    @Override
    public List<Like> getLikesByUserId(Integer userId) {
        return likeDAO.findByUserId(userId);
    }

    @Override
    public boolean isLiked(Integer articleId, Integer userId) {
        Like like = likeDAO.findByArticleAndUser(articleId, userId);
        return like != null;
    }

    @Override
    public Integer getLikeCountByArticleId(Integer articleId) {
        Integer count = likeDAO.countByArticleId(articleId);
        return count != null ? count : 0;
    }

    @Override
    public boolean addLike(Integer articleId, Integer userId) {
        // 检查是否已经点赞
        if (isLiked(articleId, userId)) {
            return false;
        }
        
        Like like = new Like();
        like.setArticleId(articleId);
        like.setUserId(userId);
        like.setCreateTime(new Date());
        
        likeDAO.insert(like);
        return true;
    }

    @Override
    public boolean cancelLike(Integer articleId, Integer userId) {
        // 检查是否已经点赞
        if (!isLiked(articleId, userId)) {
            return false;
        }
        
        likeDAO.deleteByArticleAndUser(articleId, userId);
        return true;
    }

    @Override
    public boolean toggleLike(Integer articleId, Integer userId) {
        Like like = likeDAO.findByArticleAndUser(articleId, userId);
        if (like != null) {
            // 已点赞，则取消点赞
            likeDAO.deleteByArticleAndUser(articleId, userId);
            return false; // 返回false表示当前是未点赞状态
        } else {
            // 未点赞，则添加点赞
            Like newLike = new Like();
            newLike.setArticleId(articleId);
            newLike.setUserId(userId);
            newLike.setCreateTime(new Date());
            likeDAO.insert(newLike);
            return true; // 返回true表示当前是点赞状态
        }
    }
}
