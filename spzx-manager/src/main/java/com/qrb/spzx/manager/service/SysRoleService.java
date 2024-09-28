package com.qrb.spzx.manager.service;

import com.github.pagehelper.PageInfo;
import com.qrb.spzx.model.dto.system.SysRoleDto;
import com.qrb.spzx.model.entity.system.SysRole;

import java.util.Map;

public interface SysRoleService {
    //根据角色名称进行条件分页查询
    PageInfo<SysRole> findByPage(Integer current, Integer limit, SysRoleDto sysRoleDto);

    //角色添加
    void saveSysRole(SysRole sysRole);

    //角色修改
    void updateSysRole(SysRole sysRole);

    //角色删除
    void deleteById(Long roleId);

    //为用户分配角色：查询所有角色和显示已经分配过的角色
    Map<String, Object> findAllRoles(Long userId);
}
