package com.qrb.spzx.product.mapper;

import com.qrb.spzx.model.dto.h5.ProductSkuDto;
import com.qrb.spzx.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSkuMapper {
    //2.根据畅销对商品进行排序，只获取前10条记录
    List<ProductSku> selectProductSkuBySale();

    //条件分页查询
    List<ProductSku> findByPage(ProductSkuDto productSkuDto);

    //根据skuId去获取sku信息
    ProductSku getById(Long skuId);

    //根据商品id获取商品所有sku列表
    List<ProductSku> findByProductId(Long productId);

    //更新商品sku销量
    void updateSale(Long skuId, Integer num);
}
