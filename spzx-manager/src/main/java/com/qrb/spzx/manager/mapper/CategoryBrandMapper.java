package com.qrb.spzx.manager.mapper;

import com.qrb.spzx.model.dto.product.CategoryBrandDto;
import com.qrb.spzx.model.entity.product.Brand;
import com.qrb.spzx.model.entity.product.CategoryBrand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryBrandMapper {
    //分类品牌的条件分页查询
    List<CategoryBrand> findByPage(CategoryBrandDto categoryBrandDto);

    //添加
    void save(CategoryBrand categoryBrand);

    //修改
    void update(CategoryBrand categoryBrand);

    //删除
    void deleteById(Long id);

    //根据分类id查询出对应的品牌数据
    List<Brand> findBrandByCategoryId(Long categoryId);
}
