package com.qrb.spzx.product.service.impl;

import com.alibaba.fastjson2.JSON;
import com.qrb.spzx.model.entity.product.Category;
import com.qrb.spzx.product.mapper.CategoryMapper;
import com.qrb.spzx.product.service.CategoryService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //1.查询所有的一级分类
    @Override
    public List<Category> selectOneCategory() {
        //1.先去redis中查，看是否存在数据
        String oneCategory = redisTemplate.opsForValue().get("category:one");
        //2.如果存在，则直接返回
        if(StringUtils.hasText(oneCategory)){
            //将String转换为List<Category>
            List<Category> existCategoryList = JSON.parseArray(oneCategory, Category.class);
            return existCategoryList;
        }
        //3.如果不存在，则去数据库中查找并返回给前端，还要将数据保存到redis中，并设置过期时间
        List<Category> categoryList = categoryMapper.selectOneCategory();
        //将List<Category>转换为String
        String toCategoryListString = JSON.toJSONString(categoryList);
        redisTemplate.opsForValue().set("category:one",toCategoryListString,7, TimeUnit.DAYS);
        return categoryList;
    }

    //查询所有分类，树形封装
    @Cacheable(value = "category",key = "'all'") //redis中的key: category::all
    @Override
    public List<Category> findCategoryTree() {
        // 查询所有分类（一二三级分类集合），返回list
        List<Category> allCategoryList = categoryMapper.findAll();
        //遍历所有分类，找到parentid=0的所有一级分类
        List<Category> oneCategoryList = allCategoryList
                                                    .stream()
                                                    .filter(item -> item.getParentId().longValue() == 0)
                                                    .collect(Collectors.toList());
        //遍历所有一级分类，找到 (一级分类)id  = （所有分类）parentid的所有二级分类
        oneCategoryList.forEach(oneCategory -> {
            List<Category> twoCategoryList = allCategoryList
                                                    .stream()
                                                    .filter(item -> oneCategory.getId() == item.getParentId())
                                                    .collect(Collectors.toList());
            //把二级分类封装到一级分类里面
            oneCategory.setChildren(twoCategoryList);

            //遍历所有二级分类，找到 （二级分类）id = （所有分类）parentid的所有三级分类
            twoCategoryList.forEach(twoCategory -> {
                List<Category> threeCategoryList = allCategoryList
                                                    .stream()
                                                    .filter(item -> twoCategory.getId() == item.getParentId())
                                                    .collect(Collectors.toList());
                //把三级分类封装到二级分类里面
                twoCategory.setChildren(threeCategoryList);
            });
        });
        return oneCategoryList;
    }
}
