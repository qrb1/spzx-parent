package com.qrb.spzx.product.controller;


import com.github.pagehelper.PageInfo;
import com.qrb.spzx.model.dto.h5.ProductSkuDto;
import com.qrb.spzx.model.dto.product.SkuSaleDto;
import com.qrb.spzx.model.entity.product.ProductSku;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.model.vo.h5.ProductItemVo;
import com.qrb.spzx.product.service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    //更新商品sku销量
    @PostMapping("updateSkuSaleNum")
    public Boolean updateSkuSaleNum(@RequestBody List<SkuSaleDto> skuSaleDtoList) {
        return productService.updateSkuSaleNum(skuSaleDtoList);
    }

    //根据商品id查询商品详情
    @GetMapping("item/{skuId}")
    public Result item(@PathVariable("skuId") Long skuId){
        ProductItemVo productItemVo = productService.item(skuId);
        return Result.build(productItemVo,ResultCodeEnum.SUCCESS);
    }


    //条件分页查询
    @GetMapping("{page}/{limit}")
    public Result findByPage(@PathVariable("page") Integer page,
                             @PathVariable("limit") Integer limit,
                             ProductSkuDto productSkuDto){
        PageInfo<ProductSku> pageInfo = productService.findByPage(page,limit,productSkuDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //远程调用：根据skuId查询sku信息
    @GetMapping("getBySkuId/{skuId}")
    public ProductSku getBySkuId(@PathVariable("skuId") Long skuId) {
        ProductSku productSku = productService.getBySkuId(skuId);
        return productSku;
    }
}
