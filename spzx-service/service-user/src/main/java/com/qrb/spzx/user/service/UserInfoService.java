package com.qrb.spzx.user.service;

import com.qrb.spzx.model.dto.h5.UserLoginDto;
import com.qrb.spzx.model.dto.h5.UserRegisterDto;
import com.qrb.spzx.model.vo.h5.UserInfoVo;

public interface UserInfoService {
    //保存用户注册的信息
    void register(UserRegisterDto userRegisterDto);

    //登录
    String login(UserLoginDto userLoginDto);

    //获取登录的用户的信息
    UserInfoVo getCurrentUserInfo(String token);
}
