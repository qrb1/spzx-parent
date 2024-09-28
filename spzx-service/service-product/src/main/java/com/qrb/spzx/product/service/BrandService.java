package com.qrb.spzx.product.service;

import com.qrb.spzx.model.entity.product.Brand;

import java.util.List;

public interface BrandService {
    //查询所有品牌
    List<Brand> findAll();
}
