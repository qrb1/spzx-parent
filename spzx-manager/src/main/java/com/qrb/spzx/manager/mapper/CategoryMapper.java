package com.qrb.spzx.manager.mapper;


import com.qrb.spzx.model.entity.product.Category;
import com.qrb.spzx.model.vo.product.CategoryExcelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Mapper
public interface CategoryMapper {
    //查询
    List<Category> selectCategoryByParentId(Long id);

    //判断是否有下一层，如果有设置hasChildren = true
    int selectCountByParentId(Long id);

    //2. 从数据库获取所以分类信息，返回list
    List<Category> findAll();

    //保存
    void batchInsert(List<CategoryExcelVo> categoryList);
}
