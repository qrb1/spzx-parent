package com.qrb.spzx.manager.service.impl;

import cn.hutool.core.date.DateUtil;
import com.qrb.spzx.common.exception.GuiguException;
import com.qrb.spzx.manager.properties.MinioProperties;
import com.qrb.spzx.manager.service.FileUploadService;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private MinioProperties minioProperties;

    //实现minio获取和保存用户头像
    @Override
    public String fileUpload(MultipartFile file){
        try {
            // 创建minioClient对象
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(minioProperties.getEndpointUrl()) //操作路径
                            .credentials(minioProperties.getAccessKey(), minioProperties.getSecreKey()) //用户名和密码
                            .build();

            // 判断bucket是否创建了
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) { //不存在
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            } else { //存在
                System.out.println("Bucket 'asiatrip' already exists.");
            }

            //文件上传
            String date = DateUtil.format(new Date(), "yyyyMMdd");
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            String filename = date + "/" + uuid + file.getOriginalFilename();
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioProperties.getBucketName())
                            .object(filename)
                            .stream(file.getInputStream(),file.getSize(),-1)
                            .build()
            );

            // 获取URL
            String url = minioProperties.getEndpointUrl() +"/" + minioProperties.getBucketName() + "/" + filename;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuiguException(ResultCodeEnum.SYSTEM_ERROR);
        }
    }
}
