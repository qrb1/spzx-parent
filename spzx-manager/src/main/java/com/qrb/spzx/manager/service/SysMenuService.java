package com.qrb.spzx.manager.service;

import com.qrb.spzx.model.entity.system.SysMenu;
import com.qrb.spzx.model.vo.system.SysMenuVo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SysMenuService {
    //菜单列表
    List<SysMenu> findNodes();

    //菜单添加
    void save(SysMenu sysMenu);

    //菜单修改
    void update(SysMenu sysMenu);

    //菜单删除
    void delete(Long id);

    //实现动态菜单
    List<SysMenuVo> findMenuByUserId(HttpServletRequest request);

}
