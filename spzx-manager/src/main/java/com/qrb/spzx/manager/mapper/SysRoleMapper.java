package com.qrb.spzx.manager.mapper;

import com.qrb.spzx.model.dto.system.SysRoleDto;
import com.qrb.spzx.model.entity.system.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper {
    //根据角色名称进行条件分页查询
    List<SysRole> findByPage(SysRoleDto sysRoleDto);

    //角色添加
    void save(SysRole sysRole);

    //角色修改
    void update(SysRole sysRole);

    //角色删除
    void delete(Long roleId);

    //查询所有角色
    List<SysRole> findAll();
}
