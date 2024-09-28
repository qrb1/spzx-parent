package com.qrb.spzx.manager.service.impl;

import com.alibaba.excel.EasyExcel;
import com.qrb.spzx.common.exception.GuiguException;
import com.qrb.spzx.manager.listener.ExcelListener;
import com.qrb.spzx.manager.mapper.CategoryMapper;
import com.qrb.spzx.manager.service.CategoryService;
import com.qrb.spzx.model.entity.product.Category;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.model.vo.product.CategoryExcelVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    //列表查询
    @Override
    public List<Category> findCategoryList(Long id) {
        //查询
        List<Category> categoryList = categoryMapper.selectCategoryByParentId(id);

        //判断是否有下一层，如果有设置hasChildren = true
        if(!CollectionUtils.isEmpty(categoryList)){
            categoryList.forEach(category -> {
                int count = categoryMapper.selectCountByParentId(category.getId());
                if(count > 0){
                    category.setHasChildren(true);
                }else {
                    category.setHasChildren(false);
                }
            });
        }
        return categoryList;
    }

    //Excel导出(写操作)
    @Override
    public void exportData(HttpServletResponse response) {
        try{
            //1. 设置响应的头信息和其他信息
            // 其他信息
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("分类数据", "UTF-8");
            //响应的头信息 Content-disposition:让文件以下载的方式打开
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            //2. 从数据库获取所以分类信息，返回list
            List<Category> categoryList = categoryMapper.findAll();
            //类型转换
            List<CategoryExcelVo> categoryExcelVoList = new ArrayList<>();
            // 将从数据库中查询到的Category对象转换成CategoryExcelVo对象
            for(Category category : categoryList) {
                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
                BeanUtils.copyProperties(category, categoryExcelVo);
                categoryExcelVoList.add(categoryExcelVo);
            }

            //3.实现写操作
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class).sheet("分类数据").doWrite(categoryExcelVoList);

        }catch (Exception e){
            e.printStackTrace();
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

    }

    //Excel导入（读操作）
    @Override
    public void importData(MultipartFile file) {
        //监听器
        ExcelListener<CategoryExcelVo> excelListener = new ExcelListener(categoryMapper);

        try{
            EasyExcel.read(file.getInputStream(),CategoryExcelVo.class, excelListener).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
    }
}
