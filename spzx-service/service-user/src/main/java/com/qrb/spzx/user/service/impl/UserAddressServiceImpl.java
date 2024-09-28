package com.qrb.spzx.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.qrb.spzx.model.entity.user.UserAddress;
import com.qrb.spzx.model.entity.user.UserInfo;
import com.qrb.spzx.user.mapper.UserAddressMapper;
import com.qrb.spzx.user.service.UserAddressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //获取用户地址列表
    @Override
    public List<UserAddress> findUserAddressList() {
        String token = request.getHeader("token");
        String userString = redisTemplate.opsForValue().get("user:spzx:" + token);
        UserInfo userInfo = JSON.parseObject(userString, UserInfo.class);
        Long userId = userInfo.getId();
        return userAddressMapper.findUserAddressList(userId);
    }

    //根据id获取收货地址信息
    @Override
    public UserAddress getUserAddress(Long id) {
        return userAddressMapper.getUserAddress(id);
    }
}
