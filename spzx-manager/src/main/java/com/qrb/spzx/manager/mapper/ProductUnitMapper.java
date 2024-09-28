package com.qrb.spzx.manager.mapper;

import com.qrb.spzx.model.entity.base.ProductUnit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductUnitMapper {
    //加载商品单元数据
    List<ProductUnit> findAll();
}
