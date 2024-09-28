package com.qrb.spzx.manager.mapper;

import com.qrb.spzx.model.entity.product.ProductDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDetailsMapper {
    //保存商品的详细信息到product_details表
    void save(ProductDetails productDetails);

    //根据id查询商品详细信息表 product_details
    ProductDetails selectByProductId(Long id);

    // 修改商品的详情数据 product_details表
    void updateById(ProductDetails productDetails);

    // 根据商品的id删除商品的详情数据 product_details
    void deleteByProductId(Long id);
}
