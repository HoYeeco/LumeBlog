package com.hoico.controller;

import com.hoico.model.Category;
import com.hoico.service.CategoryService;
import com.hoico.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器 - 提供文章分类管理功能
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    // 核心特性：Spring IoC/DI自动注入Service
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类
     * @return 分类列表
     */
    @GetMapping
    public ResponseResult list() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseResult.success(categories);
    }

    /**
     * 根据ID获取分类详情
     * @param id 分类ID
     * @return 分类详情
     */
    @GetMapping("/{id}")
    public ResponseResult detail(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            return ResponseResult.error("分类不存在");
        }
        return ResponseResult.success(category);
    }

    /**
     * 添加分类 - 核心特性：Spring声明式事务管理
     * @param category 分类信息
     * @return 操作结果
     */
    @Transactional
    @PostMapping
    public ResponseResult add(@RequestBody Category category) {
        // 设置创建时间和更新时间
        long now = System.currentTimeMillis();
        if (category.getClass().getDeclaredFields().length > 0) {
            try {
                category.getClass().getDeclaredField("createTime").setAccessible(true);
                category.getClass().getDeclaredField("createTime").set(category, now);
                category.getClass().getDeclaredField("updateTime").setAccessible(true);
                category.getClass().getDeclaredField("updateTime").set(category, now);
            } catch (Exception ignored) {}
        }
        
        boolean flag = categoryService.addCategory(category);
        if (flag) {
            return ResponseResult.success(category);
        } else {
            return ResponseResult.error("添加分类失败");
        }
    }

    /**
     * 更新分类 - 核心特性：Spring声明式事务管理
     * @param id 分类ID
     * @param category 分类信息
     * @return 操作结果
     */
    @Transactional
    @PutMapping("/{id}")
    public ResponseResult update(@PathVariable Integer id, @RequestBody Category category) {
        Category existing = categoryService.getCategoryById(id);
        if (existing == null) {
            return ResponseResult.error("分类不存在");
        }
        
        // 更新分类信息
        category.setId(id);
        if (category.getClass().getDeclaredFields().length > 0) {
            try {
                category.getClass().getDeclaredField("updateTime").setAccessible(true);
                category.getClass().getDeclaredField("updateTime").set(category, System.currentTimeMillis());
            } catch (Exception ignored) {}
        }
        boolean flag = categoryService.updateCategory(category);
        if (flag) {
            return ResponseResult.success(category);
        } else {
            return ResponseResult.error("更新分类失败");
        }
    }

    /**
     * 删除分类 - 核心特性：Spring声明式事务管理
     * @param id 分类ID
     * @return 操作结果
     */
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            return ResponseResult.error("分类不存在");
        }
        
        boolean flag = categoryService.deleteCategory(id);
        if (flag) {
            return ResponseResult.success();
        } else {
            return ResponseResult.error("删除分类失败");
        }
    }
}
