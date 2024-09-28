package com.qrb.spzx.manager.mapper;

import com.qrb.spzx.model.dto.system.AssginMenuDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper {
    //再次点击时会回显已经选过的菜单
    List<Long> findSysRoleMenuByRoleId(Long roleId);

    //先删除角色之前分配过的菜单
    void deleteByRoleId(Long roleId);

    //保存
    void doAssign(AssginMenuDto assginMenuDto);

    // 将该id的菜单设置为半开 isHalf=1
    void updateSysRoleMenuIsHalf(Long id);
}
