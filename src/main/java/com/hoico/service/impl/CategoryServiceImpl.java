package com.hoico.service.impl;

import com.hoico.dao.CategoryDAO;
import com.hoico.model.Category;
import com.hoico.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service // Spring IoC注解
public class CategoryServiceImpl implements CategoryService {

    @Autowired // Spring DI注解
    private CategoryDAO categoryDAO;

    @Override
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryDAO.findById(id);
    }

    @Override
    public boolean addCategory(Category category) {
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        categoryDAO.insert(category);
        return true;
    }

    @Override
    public boolean updateCategory(Category category) {
        category.setUpdateTime(new Date());
        categoryDAO.update(category);
        return true;
    }

    @Override
    public boolean deleteCategory(Integer id) {
        categoryDAO.delete(id);
        return true;
    }
}
