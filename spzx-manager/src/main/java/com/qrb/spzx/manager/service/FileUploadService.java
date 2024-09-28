package com.qrb.spzx.manager.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    //实现minio获取和保存用户头像
    String fileUpload(MultipartFile file);
}
