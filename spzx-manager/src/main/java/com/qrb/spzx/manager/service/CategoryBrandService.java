package com.qrb.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.qrb.spzx.model.dto.product.CategoryBrandDto;
import com.qrb.spzx.model.entity.product.Brand;
import com.qrb.spzx.model.entity.product.CategoryBrand;

import java.util.List;

public interface CategoryBrandService {
    //分类品牌的条件分页查询
    PageInfo<CategoryBrand> findByPage(Integer page, Integer limit, CategoryBrandDto categoryBrandDto);

    //添加
    void save(CategoryBrand categoryBrand);

    //修改
    void update(CategoryBrand categoryBrand);

    //删除
    void deleteById(Long id);

    //根据分类id查询出对应的品牌数据
    List<Brand> findBrandByCategoryId(Long categoryId);
}
