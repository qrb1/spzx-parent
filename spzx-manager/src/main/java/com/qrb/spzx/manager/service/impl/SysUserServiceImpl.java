package com.qrb.spzx.manager.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qrb.spzx.common.exception.GuiguException;
import com.qrb.spzx.manager.mapper.SysRoleUserMapper;
import com.qrb.spzx.manager.mapper.SysUserMapper;
import com.qrb.spzx.manager.service.SysUserService;
import com.qrb.spzx.model.dto.system.AssginRoleDto;
import com.qrb.spzx.model.dto.system.LoginDto;
import com.qrb.spzx.model.dto.system.SysUserDto;
import com.qrb.spzx.model.entity.system.SysUser;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //用户登录
    //LoginDto 实体类封装前端传给后端的数据
    //LoginVo 实体类封装后端传给前端的数据
    //SysUser 实体类映射数据库表的字段
    @Override
    public LoginVo login(LoginDto loginDto) {
        //1 获取前端输入的验证码和key
        String captcha = loginDto.getCaptcha();
        String codeKey = loginDto.getCodeKey();
        //2 通过key去redis中获取验证码
        String redisCode = redisTemplate.opsForValue().get("user:login:validatecode:" + codeKey);
        //3 比较输入的验证码和redis中的验证码,equalsIgnoreCase:忽略大小写的比较
        if(StrUtil.isEmpty(redisCode) || !StrUtil.equalsIgnoreCase(captcha,redisCode)){
            //4 如果不一致，输出对应的信息
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        //5 如果一致，删除redis中的验证码
        redisTemplate.delete("user:login:validatecode:" + codeKey);

        //1 获取接收前端传过来的用户信息
        String userName = loginDto.getUserName();
        //2 根据用户名查找数据库的sys_user表
        SysUser sysUser = sysUserMapper.selectUserInfoByUserName(userName);
        //3 如果没有查找到该用户，则返回错误信息
        if(sysUser == null){
            //throw new RuntimeException("用户名错误");
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        //4 如果查找到了，用户存在
        //5 将前端传过来的用户的密码与数据库的用户的密码进行比较(数据库密码已加密)
        String dataBase_Password = sysUser.getPassword();//数据库密码
        String input_Password = loginDto.getPassword();//前端输入的密码
        //对前端输入的密码进行加密，然后再进行比较md5
        input_Password = DigestUtils.md5DigestAsHex(input_Password.getBytes());
        //如果不一致，则登录失败，返回错误信息
        if(!input_Password.equals(dataBase_Password)){
            //throw new RuntimeException("用户密码错误");
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        //6 如果一致，则登录成功
        //7 登录成功，生成用户的唯一标识token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        //8 把用户信息放入到Redis中，token作为key，用户信息作为value(要将SysUser转换为String类型的json格式)
        redisTemplate.opsForValue().set("user:login:"+token, JSON.toJSONString(sysUser),7, TimeUnit.DAYS);
        //9 返回LoginVo对象（对象中封装了token）//然后前端自己去实现保存到cookie中
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }

    //获取登录的用户信息
    @Override
    public SysUser getUserInfo(String token) {
        String tokenValue = redisTemplate.opsForValue().get("user:login:" + token);
        //将String类型的JSON格式 的tokenValue转换为SysUser
        SysUser sysUser = JSON.parseObject(tokenValue,SysUser.class);
        return sysUser;
    }

    //用户退出
    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login:" + token);
    }

    //用户管理：条件分页查询
    @Override
    public PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserdto) {
        PageHelper.startPage(pageNum,pageSize);//分页
        List<SysUser> list = sysUserMapper.findByPage(sysUserdto);//条件查询
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    //用户管理：用户添加
    @Override
    public void saveSysUser(SysUser sysUser) {
        //1.判断用户是否存在
        String userName = sysUser.getUserName();//输入的用户名
        SysUser name = sysUserMapper.selectUserInfoByUserName(userName);//根据输入的用户名去数据库查询
        if(name != null){//存在
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        //2.密码进行MD5加密
        String password = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        sysUser.setPassword(password);
       //3.设置status值为1：可用 （0：不可用）
        sysUser.setStatus(1);
        sysUserMapper.save(sysUser);
    }

    //用户管理：用户修改
    @Override
    public void updateSysUser(SysUser sysUser) {
        sysUserMapper.update(sysUser);
    }

    //用户管理：用户删除
    @Override
    public void deleteById(Long userId) {
        sysUserMapper.delete(userId);
    }

    //为用户分配角色：将选中的角色保存到数据库中
    @Override
    public void doAssgin(AssginRoleDto assginRoleDto) {
        //1.根据userId删除用户之前已经分配过的角色
        sysRoleUserMapper.deleteByUserId(assginRoleDto.getUserId());

        //2.保存新的分配角色数据
        List<Long> roleIdList = assginRoleDto.getRoleIdList();
        for (Long roleId : roleIdList) {
            sysRoleUserMapper.doAssgin(assginRoleDto.getUserId(),roleId);
        }

    }
}
