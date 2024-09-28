package com.qrb.spzx.manager.mapper;

import com.qrb.spzx.model.entity.product.ProductSpec;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSpecMapper {
    //列表分页查询
    List<ProductSpec> findByPage();

    //添加
    void save(ProductSpec productSpec);

    //修改
    void update(ProductSpec productSpec);

    //删除
    void deleteById(Long id);

    //查询商品规格数据
    List<ProductSpec> findAll();
}
