package com.qrb.spzx.user.controller;

import com.qrb.spzx.model.entity.user.UserAddress;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/api/user/userAddress")
public class UserAddressController {
    @Autowired
    private UserAddressService userAddressService;

    //根据id获取收货地址信息
    @GetMapping("getUserAddress/{id}")
    public UserAddress getUserAddress(@PathVariable("id") Long id){
        return userAddressService.getUserAddress(id);
    }

    //获取用户地址列表
    @GetMapping("auth/findUserAddressList")
    public Result findUserAddressList() {
        List<UserAddress> list = userAddressService.findUserAddressList();
        return Result.build(list , ResultCodeEnum.SUCCESS) ;
    }
}
