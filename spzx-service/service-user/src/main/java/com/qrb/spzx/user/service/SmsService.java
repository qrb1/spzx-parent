package com.qrb.spzx.user.service;

public interface SmsService {
    //获取手机验证码
    void sendCode(String phone);
}
