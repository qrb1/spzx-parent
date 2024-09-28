package com.qrb.spzx.product.service.impl;

import com.qrb.spzx.model.entity.product.Brand;
import com.qrb.spzx.product.mapper.BrandMapper;
import com.qrb.spzx.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    //查询所有品牌
    @Override
    public List<Brand> findAll() {
        return brandMapper.findAll();
    }
}
