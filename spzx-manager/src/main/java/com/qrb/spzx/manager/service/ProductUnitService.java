package com.qrb.spzx.manager.service;

import com.qrb.spzx.model.entity.base.ProductUnit;

import java.util.List;

public interface ProductUnitService {
    //加载商品单元数据
    List<ProductUnit> findAll();

}
