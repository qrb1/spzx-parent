package com.qrb.spzx.manager.service.impl;

import com.qrb.spzx.manager.mapper.SysRoleMenuMapper;
import com.qrb.spzx.manager.service.SysMenuService;
import com.qrb.spzx.manager.service.SysRoleMenuService;
import com.qrb.spzx.model.dto.system.AssginMenuDto;
import com.qrb.spzx.model.entity.system.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysMenuService sysMenuService;

    //查询所有菜单 和 再次点击时会回显已经选过的菜单
    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Long roleId) {
        //查询所有菜单
        List<SysMenu> sysMenuList = sysMenuService.findNodes();

        //再次点击时会回显已经选过的菜单
        List<Long> roleMenuIds = sysRoleMenuMapper.findSysRoleMenuByRoleId(roleId);
        // 将数据存储到Map中进行返回
        Map<String , Object> result = new HashMap<>() ;
        result.put("sysMenuList" , sysMenuList) ;
        result.put("roleMenuIds" , roleMenuIds) ;
        return result;
    }

    //将选中的菜单保存到数据库中
    @Override
    public void doAssign(AssginMenuDto assginMenuDto) {
        //先删除角色之前分配过的菜单
        sysRoleMenuMapper.deleteByRoleId(assginMenuDto.getRoleId());
        //保存
        List<Map<String, Number>> menuIdList = assginMenuDto.getMenuIdList();
        if(menuIdList !=null && menuIdList.size() > 0){ //角色分配了菜单
            sysRoleMenuMapper.doAssign(assginMenuDto);
        }
    }


}
