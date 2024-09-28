package com.qrb.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.qrb.spzx.model.dto.system.AssginRoleDto;
import com.qrb.spzx.model.dto.system.LoginDto;
import com.qrb.spzx.model.dto.system.SysUserDto;
import com.qrb.spzx.model.entity.system.SysUser;
import com.qrb.spzx.model.vo.system.LoginVo;

public interface SysUserService {
    //用户登录
    //LoginDto 实体类封装前端传给后端的数据
    //LoginVo 实体类封装后端传给前端的数据
    LoginVo login(LoginDto loginDto);

    //获取登录的用户信息
    SysUser getUserInfo(String token);

    //用户退出
    void logout(String token);

    //用户管理：条件分页查询
    PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserdto);

    //用户管理：用户添加
    void saveSysUser(SysUser sysUser);

    //用户管理：用户修改
    void updateSysUser(SysUser sysUser);

    //用户管理：用户删除
    void deleteById(Long userId);

    //为用户分配角色：将选中的角色保存到数据库中
    void doAssgin(AssginRoleDto assginRoleDto);
}
