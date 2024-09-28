package com.qrb.spzx.manager.service.impl;


import com.qrb.spzx.manager.mapper.ProductUnitMapper;
import com.qrb.spzx.manager.service.ProductUnitService;
import com.qrb.spzx.model.entity.base.ProductUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUnitServiceImpl implements ProductUnitService {
    @Autowired
    private ProductUnitMapper productUnitMapper;

    //加载商品单元数据
    @Override
    public List<ProductUnit> findAll() {
        return productUnitMapper.findAll();
    }
}
