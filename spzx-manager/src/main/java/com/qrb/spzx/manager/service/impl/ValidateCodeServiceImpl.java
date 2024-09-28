package com.qrb.spzx.manager.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.qrb.spzx.manager.service.ValidateCodeService;
import com.qrb.spzx.model.vo.system.ValidateCodeVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //生成图片验证码
    //ValidateCodeVo 封装要返回给前端的验证码的key 和 value
    @Override
    public ValidateCodeVo generateValidateCode() {
        //1 使用hutool 工具生成验证码
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 20);
        //2 获取验证码的值和图片
        String code = circleCaptcha.getCode();//验证码的值
        String imageBase64 = circleCaptcha.getImageBase64();//整个验证码图片
        //3 使用UUID生成一个唯一标识
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        //4 唯一标识作为Redis的key 验证码的值作为Redis的value，并设置有效时间
        redisTemplate.opsForValue().set("user:login:validatecode:"+key,code,5, TimeUnit.MINUTES);
        //5 将唯一标识设置为ValidateCodeVo的 key 将获取的图片作为ValidateCodeVo的value并返回
        ValidateCodeVo validateCodeVo = new ValidateCodeVo();
        validateCodeVo.setCodeKey(key);
        validateCodeVo.setCodeValue("data:image/png;base64," + imageBase64);
        return validateCodeVo;
    }
}
