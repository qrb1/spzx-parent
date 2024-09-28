package com.qrb.spzx.manager.controller;


import com.github.pagehelper.PageInfo;
import com.qrb.spzx.manager.service.ProductSpecService;
import com.qrb.spzx.model.entity.product.ProductSpec;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(allowCredentials = "true",originPatterns = "*",allowedHeaders = "*")
@RestController
@RequestMapping(value="/admin/product/productSpec")
public class ProductSpecController {
    @Autowired
    private ProductSpecService productSpecService;

    //查询商品规格数据
    @GetMapping("findAll")
    public Result findAll() {
        List<ProductSpec> list = productSpecService.findAll();
        return Result.build(list , ResultCodeEnum.SUCCESS) ;
    }

    //列表分页查询
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable("page") Integer page,
                       @PathVariable("limit") Integer limit){
        PageInfo<ProductSpec> pageInfo = productSpecService.findByPage(page,limit);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //添加
    @PostMapping("save")
    public Result save(@RequestBody ProductSpec productSpec){
        productSpecService.save(productSpec);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //修改
    @PutMapping("update")
    public Result update(@RequestBody ProductSpec productSpec){
        productSpecService.update(productSpec);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //删除
    @DeleteMapping("deleteById/{id}")
    public Result deleteById(@PathVariable("id") Long id){
        productSpecService.deleteById(id);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
}
