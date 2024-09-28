package com.qrb.spzx.product.controller;


import com.qrb.spzx.model.entity.product.Category;
import com.qrb.spzx.model.entity.product.Product;
import com.qrb.spzx.model.entity.product.ProductSku;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.model.vo.h5.IndexVo;
import com.qrb.spzx.product.service.CategoryService;
import com.qrb.spzx.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "首页接口管理")
@RestController
@RequestMapping(value="/api/product/index")
//@CrossOrigin(allowCredentials = "true",originPatterns = "*",allowedHeaders = "*")
public class IndexController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result index(){
        //1.查询所有的一级分类
        List<Category> categoryList = categoryService.selectOneCategory();

        //2.根据畅销对商品进行排序，只获取前10条记录
        List<ProductSku> productSkuList = productService.selectProductSkuBySale();

        //3.封装对象
        IndexVo indexVo = new IndexVo();
        indexVo.setCategoryList(categoryList);
        indexVo.setProductSkuList(productSkuList);
        return Result.build(indexVo, ResultCodeEnum.SUCCESS);
    }
}
