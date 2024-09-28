package com.qrb.spzx.manager.mapper;

import com.qrb.spzx.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSkuMapper {
    //保存商品的SKU信息到product_sku表
    void save(ProductSku productSku);

    //根据id查询商品sku表 product_sku
    List<ProductSku> selectByProductId(Long id);

    // 修改商品的sku数据 product_sku表
    void updateById(ProductSku productSku);

    // 根据商品id删除商品的sku数据 product_sku
    void deleteByProductId(Long id);
}
