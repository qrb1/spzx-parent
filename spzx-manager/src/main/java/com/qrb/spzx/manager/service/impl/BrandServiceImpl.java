package com.qrb.spzx.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qrb.spzx.manager.mapper.BrandMapper;
import com.qrb.spzx.manager.service.BrandService;
import com.qrb.spzx.model.entity.product.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;
    //列表分页查询
    @Override
    public PageInfo<Brand> findByPage(Integer page, Integer limit) {
        //设置分页
        PageHelper.startPage(page,limit);
        //查询
        List<Brand> list = brandMapper.findByPage();
        //封装数据
        PageInfo<Brand> brandPageInfo = new PageInfo<>(list);
        //返回
        return brandPageInfo;
    }

    //添加
    @Override
    public void save(Brand brand) {
        brandMapper.save(brand);
    }

    //修改
    @Override
    public void update(Brand brand) {
        brandMapper.update(brand);
    }

    //删除
    @Override
    public void deleteById(Long id) {
        brandMapper.deleteById(id);
    }

    //查询所有品牌
    @Override
    public List<Brand> findAll() {
        List<Brand> list = brandMapper.findAll();
        return list;
    }
}
