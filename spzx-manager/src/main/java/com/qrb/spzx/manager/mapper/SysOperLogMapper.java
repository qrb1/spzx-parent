package com.qrb.spzx.manager.mapper;

import com.qrb.spzx.model.entity.system.SysOperLog;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface SysOperLogMapper {
    void insert(SysOperLog sysOperLog);
}
