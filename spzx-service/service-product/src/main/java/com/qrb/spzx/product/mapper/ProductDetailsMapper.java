package com.qrb.spzx.product.mapper;

import com.qrb.spzx.model.entity.product.ProductDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDetailsMapper {
    //根据productId获取商品详细信息
    ProductDetails getByProductId(Long productId);
}
