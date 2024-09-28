package com.qrb.spzx.manager.controller;

import com.qrb.spzx.manager.service.FileUploadService;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//实现minio获取和保存用户头像
@RestController
@CrossOrigin(allowCredentials = "true",originPatterns = "*",allowedHeaders = "*")
@RequestMapping("/admin/system")
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file){
        //1.获取上传文件 MultipartFile file
        //2.调用service方法，并返回url
        String url = fileUploadService.fileUpload(file);
        return Result.build(url, ResultCodeEnum.SUCCESS);
    }
}
