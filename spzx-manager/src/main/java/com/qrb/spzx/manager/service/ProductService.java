package com.qrb.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.qrb.spzx.model.dto.product.ProductDto;
import com.qrb.spzx.model.entity.product.Product;

public interface ProductService {
    //列表条件分页查询
    PageInfo<Product> findByPage(Integer page, Integer limit, ProductDto productDto);

    //商品管理：添加商品信息到数据库
    void save(Product product);

    //根据商品id查询商品信息
    Product getById(Long id);

    //保存修改
    void update(Product product);

    //删除
    void deleteById(Long id);

    //商品审核
    void updateAuditStatus(Long id, Integer auditStatus);

    //商品上下架
    void updateStatus(Long id, Integer status);
}
