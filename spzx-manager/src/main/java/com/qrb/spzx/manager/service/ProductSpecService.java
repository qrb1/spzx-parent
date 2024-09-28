package com.qrb.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.qrb.spzx.model.entity.product.ProductSpec;

import java.util.List;

public interface ProductSpecService {
    //列表分页查询
    PageInfo<ProductSpec> findByPage(Integer page, Integer limit);

    //添加
    void save(ProductSpec productSpec);

    //修改
    void update(ProductSpec productSpec);

    //删除
    void deleteById(Long id);

    //查询商品规格数据
    List<ProductSpec> findAll();
}
