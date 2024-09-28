package com.qrb.spzx.user.mapper;

import com.qrb.spzx.model.entity.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {
    //注册的用户名不能重复
    UserInfo selectByUserName(String username);

    //保存到数据库
    void save(UserInfo userInfo);
}
