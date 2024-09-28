package com.qrb.spzx.manager.service.impl;

import com.qrb.spzx.common.exception.GuiguException;
import com.qrb.spzx.manager.mapper.SysMenuMapper;
import com.qrb.spzx.manager.mapper.SysRoleMenuMapper;
import com.qrb.spzx.manager.service.SysMenuService;
import com.qrb.spzx.manager.service.SysUserService;
import com.qrb.spzx.manager.utils.MenuHelper;
import com.qrb.spzx.model.entity.system.SysMenu;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.model.vo.system.SysMenuVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    //菜单列表
    @Override
    public List<SysMenu> findNodes() {
        //查询所有菜单
        List<SysMenu> list = sysMenuMapper.findAll();
        //调用工具类的方法，返回要求的数据格式
        List<SysMenu> sysMenus = MenuHelper.buildTree(list);
        return sysMenus;
    }

    //菜单添加
    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.save(sysMenu);
        //当新添加一个子菜单后，将父菜单的isHalf变为1
        updateSysRoleMenu(sysMenu);
    }

    //当新添加一个子菜单后，将父菜单的isHalf变为1
    private void updateSysRoleMenu(SysMenu sysMenu) {
        //获取当前添加的菜单的父菜单
        SysMenu parentMenu = sysMenuMapper.selectParentMenu(sysMenu.getParentId());
        if(parentMenu != null){
            // 将该id的菜单设置为半开 isHalf=1
            sysRoleMenuMapper.updateSysRoleMenuIsHalf(parentMenu.getId()) ;
            // 递归调用
            updateSysRoleMenu(parentMenu) ;
        }
    }

    //菜单修改
    @Override
    public void update(SysMenu sysMenu) {
        sysMenuMapper.update(sysMenu);
    }

    //菜单删除
    @Override
    public void delete(Long id) {
        //先判断该菜单下有没有子菜单
        int count = sysMenuMapper.selectCountById(id);
        //如果有，则不能删除 count>0
        if(count > 0){
            throw new GuiguException(ResultCodeEnum.NODE_ERROR);
        }
        //如果没有，则可以删除 count=0
        sysMenuMapper.delete(id);
    }

    //实现动态菜单
    @Override
    public List<SysMenuVo> findMenuByUserId(HttpServletRequest request) {
        //获取当前用户id
        String token = request.getHeader("token");
        Long userId = sysUserService.getUserInfo(token).getId();

        //根据userId查询用户可以操作的菜单
        List<SysMenu> menuList = sysMenuMapper.findMenuByUserId(userId);

        //封装数据进行返回
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(menuList);
        List<SysMenuVo> sysMenuVos = this.buildMenus(sysMenuTreeList);
        return sysMenuVos;
    }

    // 将List<SysMenu>对象转换成List<SysMenuVo>对象
    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }
}
