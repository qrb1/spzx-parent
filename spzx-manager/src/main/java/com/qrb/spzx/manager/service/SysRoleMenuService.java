package com.qrb.spzx.manager.service;

import com.qrb.spzx.model.dto.system.AssginMenuDto;

import java.util.Map;

public interface SysRoleMenuService {
    //查询所有菜单 和 再次点击时会回显已经选过的菜单
    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);

    //将选中的菜单保存到数据库中
    void doAssign(AssginMenuDto assginMenuDto);

}
