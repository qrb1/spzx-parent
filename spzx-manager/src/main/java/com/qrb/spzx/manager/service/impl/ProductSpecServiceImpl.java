package com.qrb.spzx.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qrb.spzx.manager.mapper.ProductSpecMapper;
import com.qrb.spzx.manager.service.ProductSpecService;
import com.qrb.spzx.model.entity.product.ProductSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpecServiceImpl implements ProductSpecService {
    @Autowired
    private ProductSpecMapper productSpecMapper;

    //列表分页查询
    @Override
    public PageInfo<ProductSpec> findByPage(Integer page, Integer limit) {
        //设置分页
        PageHelper.startPage(page,limit);
        //查询
        List<ProductSpec> list = productSpecMapper.findByPage();
        //封装数据
        PageInfo<ProductSpec> pageInfo = new PageInfo<>(list);
        //返回
        return pageInfo;
    }

    //添加
    @Override
    public void save(ProductSpec productSpec) {
        productSpecMapper.save(productSpec);
    }

    //修改
    @Override
    public void update(ProductSpec productSpec) {
        productSpecMapper.update(productSpec);
    }

    //删除
    @Override
    public void deleteById(Long id) {
        productSpecMapper.deleteById(id);
    }

    //查询商品规格数据
    @Override
    public List<ProductSpec> findAll() {
        return productSpecMapper.findAll();
    }
}
