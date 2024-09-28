package com.qrb.spzx.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qrb.spzx.manager.mapper.SysRoleMapper;
import com.qrb.spzx.manager.mapper.SysRoleUserMapper;
import com.qrb.spzx.manager.service.SysRoleService;
import com.qrb.spzx.model.dto.system.SysRoleDto;
import com.qrb.spzx.model.entity.system.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    //根据角色名称进行条件分页查询
    // TODO 查询出来的创建时间与数据库的不一致
    @Override
    public PageInfo<SysRole> findByPage(Integer current, Integer limit, SysRoleDto sysRoleDto) {
        //设置分页
        PageHelper.startPage(current,limit);
        //条件查询
        List<SysRole> RoleList =  sysRoleMapper.findByPage(sysRoleDto);
        //封装数据
        PageInfo<SysRole> pageInfo = new PageInfo<>(RoleList);
        return pageInfo;
    }

    //角色添加
    @Override
    public void saveSysRole(SysRole sysRole) {
        sysRoleMapper.save(sysRole);
    }

    //角色修改
    @Override
    public void updateSysRole(SysRole sysRole) {
        sysRoleMapper.update(sysRole);
    }

    //角色删除
    @Override
    public void deleteById(Long roleId) {
        sysRoleMapper.delete(roleId);
    }

    //为用户分配角色：查询所有角色和显示已经分配过的角色
    @Override
    public Map<String, Object> findAllRoles(Long userId) {
        //1.查询所有角色
        List<SysRole> list = sysRoleMapper.findAll();

        //2.根据用户id显示已经分配过的角色
        List<Long> listRoleIds = sysRoleUserMapper.selectRoleIdsByUserId(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("allRolesList",list);
        map.put("sysUserRoles",listRoleIds);
        return map;
    }
}
