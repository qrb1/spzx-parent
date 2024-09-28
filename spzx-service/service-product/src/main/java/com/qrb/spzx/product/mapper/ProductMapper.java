package com.qrb.spzx.product.mapper;

import com.qrb.spzx.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    //通过获取的sku信息获取productId,获取商品信息
    Product getById(Long productId);
}
