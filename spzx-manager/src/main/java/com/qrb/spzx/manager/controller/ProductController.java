package com.qrb.spzx.manager.controller;


import com.github.pagehelper.PageInfo;
import com.qrb.spzx.manager.service.ProductService;
import com.qrb.spzx.model.dto.h5.ProductSkuDto;
import com.qrb.spzx.model.dto.product.ProductDto;
import com.qrb.spzx.model.entity.product.Product;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.math3.util.ResizableDoubleArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(allowCredentials = "true",originPatterns = "*",allowedHeaders = "*")
@RestController
@RequestMapping(value="/admin/product/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    //商品上下架
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,
                               @PathVariable Integer status) {
        productService.updateStatus(id, status);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    //商品审核
    @GetMapping("/updateAuditStatus/{id}/{auditStatus}")
    public Result updateAuditStatus(@PathVariable Long id,
                                    @PathVariable Integer auditStatus) {
        productService.updateAuditStatus(id, auditStatus);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    //删除
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    //保存修改
    @PutMapping("updateById")
    public Result update(@RequestBody Product product){
        productService.update(product);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //根据商品id查询商品信息
    @GetMapping("getById/{id}")
    public Result getById(@PathVariable("id") Long id){
        Product product = productService.getById(id);
        return Result.build(product,ResultCodeEnum.SUCCESS);
    }

    //商品管理：添加商品信息到数据库
    @PostMapping("save")
    public Result save(@RequestBody Product product){
        productService.save(product);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //列表条件分页查询
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable("page") Integer page,
                       @PathVariable("limit") Integer limit,
                       ProductDto productDto){
        PageInfo<Product> pageInfo = productService.findByPage(page,limit,productDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }
}
