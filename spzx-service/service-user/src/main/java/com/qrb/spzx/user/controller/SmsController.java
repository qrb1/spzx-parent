package com.qrb.spzx.user.controller;


import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.user.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    //获取手机验证码
    @GetMapping(value = "/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone") String phone){
        smsService.sendCode(phone);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
