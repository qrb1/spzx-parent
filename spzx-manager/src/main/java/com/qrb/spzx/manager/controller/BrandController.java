package com.qrb.spzx.manager.controller;


import com.github.pagehelper.PageInfo;
import com.qrb.spzx.common.log.annotation.Log;
import com.qrb.spzx.common.log.enums.OperatorType;
import com.qrb.spzx.manager.service.BrandService;
import com.qrb.spzx.model.entity.product.Brand;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(allowCredentials = "true",originPatterns = "*",allowedHeaders = "*")
@RestController
@RequestMapping(value="/admin/product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    //查询所有品牌
    @GetMapping("findAll")
    public Result findAll(){
        List<Brand> list = brandService.findAll();
        return Result.build(list,ResultCodeEnum.SUCCESS);
    }

    @Log(title = "品牌管理：列表",businessType = 0,operatorType = OperatorType.OTHER)
    //列表分页查询
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable("page") Integer page,
                       @PathVariable("limit") Integer limit){
        PageInfo<Brand> pageInfo = brandService.findByPage(page,limit);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //添加
    @PostMapping("save")
    public Result save(@RequestBody Brand brand){
        brandService.save(brand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //修改
    @PutMapping("update")
    public Result update(@RequestBody Brand brand){
        brandService.update(brand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //删除
    @DeleteMapping("deleteById/{id}")
    public Result deleteById(@PathVariable("id") Long id){
        brandService.deleteById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
