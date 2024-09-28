package com.qrb.spzx.manager.controller;


import com.qrb.spzx.manager.service.CategoryService;
import com.qrb.spzx.model.entity.product.Category;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*
*       分类管理
* */

@CrossOrigin(allowCredentials = "true",originPatterns = "*",allowedHeaders = "*")
@RestController
@RequestMapping(value="/admin/product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //Excel导入（读操作）
    @PostMapping("importData")
    public Result importData(MultipartFile file){
        //获取上传的文件 MultipartFile file
        categoryService.importData(file);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //Excel导出(写操作)
    @GetMapping("exportData")
    public void exportData(HttpServletResponse response){
        categoryService.exportData(response);
    }

    //列表查询
    //每次只查一层数据
    @GetMapping("findCategoryList/{id}")
    public Result findCategoryList(@PathVariable("id") Long id){
        List<Category> list = categoryService.findCategoryList(id);
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
