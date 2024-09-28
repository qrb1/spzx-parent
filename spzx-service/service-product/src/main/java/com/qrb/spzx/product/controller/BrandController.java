package com.qrb.spzx.product.controller;


import com.qrb.spzx.model.entity.product.Brand;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/api/product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    //查询所有品牌
    @GetMapping("findAll")
    public Result findAll(){
        List<Brand> list = brandService.findAll();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
