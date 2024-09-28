package com.qrb.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.qrb.spzx.model.entity.product.Brand;

import java.util.List;

public interface BrandService {
    //列表分页查询
    PageInfo<Brand> findByPage(Integer page, Integer limit);

    //添加
    void save(Brand brand);

    //修改
    void update(Brand brand);

    //删除
    void deleteById(Long id);

    //查询所有品牌
    List<Brand> findAll();

}
