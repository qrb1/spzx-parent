package com.qrb.spzx.user.controller;


import com.qrb.spzx.model.dto.h5.UserLoginDto;
import com.qrb.spzx.model.dto.h5.UserRegisterDto;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.model.vo.h5.UserInfoVo;
import com.qrb.spzx.user.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user/userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    // TODO 登录校验 暂时不做 13节3.4小节 P93集-95集

    //获取登录的用户的信息
    @GetMapping("auth/getCurrentUserInfo")
    public Result<UserInfoVo> getCurrentUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        UserInfoVo userInfoVo = userInfoService.getCurrentUserInfo(token);
        return Result.build(userInfoVo,ResultCodeEnum.SUCCESS);
    }

    //登录
    @PostMapping("login")
    public Result login(@RequestBody UserLoginDto userLoginDto){
        String token = userInfoService.login(userLoginDto);
        return Result.build(token,ResultCodeEnum.SUCCESS);
    }

    //保存用户注册的信息
    @PostMapping("register")
    public Result register(@RequestBody UserRegisterDto userRegisterDto){
        userInfoService.register(userRegisterDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
