package com.qrb.spzx.manager;

import com.qrb.spzx.common.log.annotation.EnableLogAspect;
import com.qrb.spzx.manager.properties.MinioProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableLogAspect
@SpringBootApplication
@ComponentScan(basePackages = {"com.qrb.spzx"})
@EnableConfigurationProperties(value = {MinioProperties.class})
@EnableScheduling //启动定时器
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class,args);
    }
}
