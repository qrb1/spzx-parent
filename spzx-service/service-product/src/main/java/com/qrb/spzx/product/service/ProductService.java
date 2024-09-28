package com.qrb.spzx.product.service;

import com.github.pagehelper.PageInfo;
import com.qrb.spzx.model.dto.h5.ProductSkuDto;
import com.qrb.spzx.model.dto.product.SkuSaleDto;
import com.qrb.spzx.model.entity.product.ProductSku;
import com.qrb.spzx.model.vo.h5.ProductItemVo;

import java.util.List;

public interface ProductService {
    //2.根据畅销对商品进行排序，只获取前10条记录
    List<ProductSku> selectProductSkuBySale();

    //条件分页查询
    PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto);

    //根据商品id查询商品详情
    ProductItemVo item(Long skuId);

    //远程调用：根据skuId查询sku信息
    ProductSku getBySkuId(Long skuId);

    //更新商品sku销量
    Boolean updateSkuSaleNum(List<SkuSaleDto> skuSaleDtoList);
}
