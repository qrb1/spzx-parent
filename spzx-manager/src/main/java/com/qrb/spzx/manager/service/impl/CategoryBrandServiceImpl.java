package com.qrb.spzx.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qrb.spzx.manager.mapper.CategoryBrandMapper;
import com.qrb.spzx.manager.service.CategoryBrandService;
import com.qrb.spzx.model.dto.product.CategoryBrandDto;
import com.qrb.spzx.model.entity.product.Brand;
import com.qrb.spzx.model.entity.product.CategoryBrand;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryBrandServiceImpl implements CategoryBrandService {

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    //分类品牌的条件分页查询
    @Override
    public PageInfo<CategoryBrand> findByPage(Integer page, Integer limit, CategoryBrandDto categoryBrandDto) {
        //设置分页
        PageHelper.startPage(page,limit);
        //条件查询
        List<CategoryBrand> list= categoryBrandMapper.findByPage(categoryBrandDto);
        //封装数据
        PageInfo<CategoryBrand> pageInfo = new PageInfo<>(list);
        //返回
        return pageInfo;
    }

    //添加
    @Override
    public void save(CategoryBrand categoryBrand) {
        categoryBrandMapper.save(categoryBrand);
    }

    //修改
    @Override
    public void update(CategoryBrand categoryBrand) {
        categoryBrandMapper.update(categoryBrand);
    }

    //删除
    @Override
    public void deleteById(Long id) {
        categoryBrandMapper.deleteById(id);
    }

    //根据分类id查询出对应的品牌数据
    @Override
    public List<Brand> findBrandByCategoryId(Long categoryId) {
        return categoryBrandMapper.findBrandByCategoryId(categoryId);
    }
}
