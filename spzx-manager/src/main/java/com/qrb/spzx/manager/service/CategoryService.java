package com.qrb.spzx.manager.service;

import com.qrb.spzx.model.entity.product.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    //列表查询
    List<Category> findCategoryList(Long id);

    //Excel导出(写操作)
    void exportData(HttpServletResponse response);

    //Excel导入（读操作）
    void importData(MultipartFile file);
}
