package com.qrb.spzx.manager.mapper;

import com.qrb.spzx.model.dto.system.SysUserDto;
import com.qrb.spzx.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@Mapper
public interface SysUserMapper {
    //2 根据用户名查找数据库的sys_user表
    SysUser selectUserInfoByUserName(String userName);

    //用户管理：条件分页查询
    List<SysUser> findByPage(SysUserDto sysUserdto);

    //用户管理：用户添加
    void save(SysUser sysUser);

    //用户管理：用户修改
    void update(SysUser sysUser);

    //用户管理：用户删除
    void delete(Long userId);
}
