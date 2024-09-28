package com.qrb.spzx.manager.mapper;

import com.qrb.spzx.model.entity.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    //查询所有菜单
    List<SysMenu> findAll();

    //菜单添加
    void save(SysMenu sysMenu);

    //菜单修改
    void update(SysMenu sysMenu);

    //先判断该菜单下有没有子菜单
    int selectCountById(Long id);

    //如果没有，则可以删除 count=0
    void delete(Long id);

    //根据userId查询用户可以操作的菜单
    List<SysMenu> findMenuByUserId(Long userId);

    //获取当前添加的菜单的父菜单
    SysMenu selectParentMenu(Long parentId);
}
