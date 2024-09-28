package com.qrb.spzx.feign.product;

import com.qrb.spzx.model.dto.product.SkuSaleDto;
import com.qrb.spzx.model.entity.order.OrderInfo;
import com.qrb.spzx.model.entity.product.ProductSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//要进行远程调用的接口所在微服务的名字
@FeignClient(value = "service-product")
public interface ProductFeignClient {

    //远程调用：根据skuId查询sku信息
    @GetMapping("/api/product/getBySkuId/{skuId}")
    public ProductSku getBySkuId(@PathVariable("skuId") Long skuId) ;

    //更新商品sku销量
    @PostMapping("/api/product/updateSkuSaleNum")
    Boolean updateSkuSaleNum(@RequestBody List<SkuSaleDto> skuSaleDtoList);

}
