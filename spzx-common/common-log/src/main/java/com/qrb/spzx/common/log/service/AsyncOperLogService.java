package com.qrb.spzx.common.log.service;

import com.qrb.spzx.model.entity.system.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

public interface AsyncOperLogService {			// 保存日志数据
    void saveSysOperLog(SysOperLog sysOperLog) ;
}


