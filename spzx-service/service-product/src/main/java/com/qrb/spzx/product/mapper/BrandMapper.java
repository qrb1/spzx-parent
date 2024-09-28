package com.qrb.spzx.product.mapper;

import com.qrb.spzx.model.entity.product.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface BrandMapper {


    //查询所有品牌
    List<Brand> findAll();
}
