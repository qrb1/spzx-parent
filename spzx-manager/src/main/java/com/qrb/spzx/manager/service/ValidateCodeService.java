package com.qrb.spzx.manager.service;

import com.qrb.spzx.model.vo.system.ValidateCodeVo;

public interface ValidateCodeService {
    //生成图片验证码
    //ValidateCodeVo 封装要存入Redis中的key 和 value
    ValidateCodeVo generateValidateCode();
}
