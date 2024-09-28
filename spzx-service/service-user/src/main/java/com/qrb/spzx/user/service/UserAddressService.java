package com.qrb.spzx.user.service;

import com.qrb.spzx.model.entity.user.UserAddress;

import java.util.List;

public interface UserAddressService {
    //获取用户地址列表
    List<UserAddress> findUserAddressList();

    //根据id获取收货地址信息
    UserAddress getUserAddress(Long id);
}
