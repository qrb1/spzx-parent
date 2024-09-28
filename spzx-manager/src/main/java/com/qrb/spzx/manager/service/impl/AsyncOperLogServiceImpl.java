package com.qrb.spzx.manager.service.impl;

import com.qrb.spzx.common.log.service.AsyncOperLogService;
import com.qrb.spzx.manager.mapper.SysOperLogMapper;
import com.qrb.spzx.model.entity.system.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncOperLogServiceImpl implements AsyncOperLogService {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    //保存日志数据
    @Override
    public void saveSysOperLog(SysOperLog sysOperLog) {
        sysOperLogMapper.insert(sysOperLog);
    }

}
