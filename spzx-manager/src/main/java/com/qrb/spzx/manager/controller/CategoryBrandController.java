package com.qrb.spzx.manager.controller;


import com.github.pagehelper.PageInfo;
import com.qrb.spzx.manager.service.CategoryBrandService;
import com.qrb.spzx.model.dto.product.CategoryBrandDto;
import com.qrb.spzx.model.entity.product.Brand;
import com.qrb.spzx.model.entity.product.CategoryBrand;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(allowCredentials = "true",originPatterns = "*",allowedHeaders = "*")
@RestController
@RequestMapping(value = "/admin/product/categoryBrand")
public class CategoryBrandController {
    @Autowired
    private CategoryBrandService categoryBrandService;

    //根据分类id查询出对应的品牌数据
    @GetMapping("findBrandByCategoryId/{categoryId}")
    public Result findBrandByCategoryId(@PathVariable("categoryId") Long categoryId){
        List<Brand> list = categoryBrandService.findBrandByCategoryId(categoryId);
        return Result.build(list,ResultCodeEnum.SUCCESS);
    }

    //修改
    @PutMapping("update")
    public Result update(@RequestBody CategoryBrand categoryBrand){
        categoryBrandService.update(categoryBrand);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //删除
    @DeleteMapping("deleteById/{id}")
    public Result deleteById(@PathVariable("id") Long id){
        categoryBrandService.deleteById(id);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }


    //添加
    @PostMapping("save")
    public Result save(@RequestBody CategoryBrand categoryBrand){
        categoryBrandService.save(categoryBrand);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //分类品牌的条件分页查询
    @GetMapping("{page}/{limit}")
    public Result findByPage(@PathVariable("page") Integer page,
                             @PathVariable("limit") Integer limit,
                             CategoryBrandDto categoryBrandDto){
        PageInfo<CategoryBrand> categoryBrand = categoryBrandService.findByPage(page,limit,categoryBrandDto);
        return Result.build(categoryBrand, ResultCodeEnum.SUCCESS);

    }
}
