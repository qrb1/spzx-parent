package com.qrb.spzx.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.qrb.spzx.common.exception.GuiguException;
import com.qrb.spzx.model.dto.h5.UserLoginDto;
import com.qrb.spzx.model.dto.h5.UserRegisterDto;
import com.qrb.spzx.model.entity.user.UserInfo;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.model.vo.h5.UserInfoVo;
import com.qrb.spzx.user.mapper.UserInfoMapper;
import com.qrb.spzx.user.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String , String> redisTemplate;

    //保存用户注册的信息
    @Override
    public void register(UserRegisterDto userRegisterDto) {
        //从userRegisterDto中获取数据
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String nickName = userRegisterDto.getNickName();
        String code = userRegisterDto.getCode();
        //比较输入的验证码和redis中的验证码
        String redisCode = redisTemplate.opsForValue().get(username);
        if (!redisCode.equals(code)){
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        //注册的用户名不能重复
        UserInfo userInfo = userInfoMapper.selectByUserName(username);
        if(userInfo != null){
            //用户已存在
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        //封装添加数据，保存到数据库
        userInfo = new UserInfo(); //要有这个，否则会报空指针异常
        userInfo.setUsername(username);
        userInfo.setNickName(nickName);
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userInfo.setPhone(username);
        userInfo.setStatus(1);
        userInfo.setSex(0);
        userInfo.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        userInfoMapper.save(userInfo);

        //删除redis中的验证码
        redisTemplate.delete(username);
    }

    //登录
    @Override
    public String login(UserLoginDto userLoginDto) {
        //获取用户输入的用户名和密码
        String username = userLoginDto.getUsername();
        String user_password = userLoginDto.getPassword();
        //根据用户名去数据库中查
        UserInfo userInfo = userInfoMapper.selectByUserName(username);
        //判断用户是否存在
        if(userInfo == null){//不存在
            throw new GuiguException(ResultCodeEnum.USER_IS_NOT_EXISTS);
        }
        //判断密码是否一致
        String db_password = userInfo.getPassword();
        user_password = DigestUtils.md5DigestAsHex(user_password.getBytes());
        if(!db_password.equals(user_password)){
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        //判断用户是否被禁用
        if (userInfo.getStatus() == 0){
            throw new GuiguException(ResultCodeEnum.ACCOUNT_STOP);
        }
        //生成token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        //将用户信息放入到Redis中（key:token ,value:用户信息）
        redisTemplate.opsForValue().set("user:spzx:"+token, JSON.toJSONString(userInfo),7, TimeUnit.DAYS);
        //将token返回
        return token;
    }

    //获取登录的用户的信息
    @Override
    public UserInfoVo getCurrentUserInfo(String token) {
        //根据token去Redis中获取用户信息
        String userInfoSting = redisTemplate.opsForValue().get("user:spzx:" + token);
        if(!StringUtils.hasText(userInfoSting)){
            throw new GuiguException(ResultCodeEnum.LOGIN_AUTH);
        }
        //封装数据
        UserInfo userInfo = JSON.parseObject(userInfoSting, UserInfo.class); //set的时候是UserInfo类型
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo,userInfoVo);
        //返回
        return userInfoVo;
    }
}
