package com.qrb.spzx.manager.mapper;

import com.qrb.spzx.model.dto.product.ProductDto;
import com.qrb.spzx.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    //列表条件分页查询
    List<Product> findByPage(ProductDto productDto);

    //保存商品的信息到product表
    void save(Product product);

    //根据id查询商品表 product
    Product selectById(Long id);

    // 修改商品基本数据 product表
    void updateById(Product product);

    // 根据id删除商品基本数据 product
    void deleteById(Long id);
}
