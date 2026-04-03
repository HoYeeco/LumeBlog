package com.hoico.service;

import com.hoico.model.Category;

import java.util.List;

public interface CategoryService {
    // 获取所有分类
    List<Category> getAllCategories();
    
    // 根据ID获取分类
    Category getCategoryById(Integer id);
    
    // 添加分类
    boolean addCategory(Category category);
    
    // 更新分类
    boolean updateCategory(Category category);
    
    // 删除分类
    boolean deleteCategory(Integer id);
}
