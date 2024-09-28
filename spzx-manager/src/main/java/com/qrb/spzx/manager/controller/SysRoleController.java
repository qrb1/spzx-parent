package com.qrb.spzx.manager.controller;

import com.github.pagehelper.PageInfo;
import com.qrb.spzx.manager.service.SysRoleService;
import com.qrb.spzx.model.dto.system.LoginDto;
import com.qrb.spzx.model.dto.system.SysRoleDto;
import com.qrb.spzx.model.entity.system.SysRole;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/*
*   //SysRoleDto 实体类封装前端传给后端的数据
* */
@RestController
@RequestMapping(value = "/admin/system/sysRole")
@CrossOrigin(allowCredentials = "true" , originPatterns = "*" , allowedHeaders = "*")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    //为用户分配角色：查询所有角色和显示已经分配过的角色
    @GetMapping("findAllRoles/{userId}")
    public Result findAllRoles(@PathVariable("userId") Long userId){
        //String:封装所有角色
        //Object:封装已经分配过的角色
        //封装到map中，返回给前端进行处理即可
        Map<String, Object> map = sysRoleService.findAllRoles(userId);
        return Result.build(map,ResultCodeEnum.SUCCESS);
    }



    //角色删除
    @DeleteMapping("deleteById/{roleId}")
    public Result deleteById(@PathVariable("roleId") Long roleId){
        sysRoleService.deleteById(roleId);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //角色修改
    @PutMapping("updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole){
        sysRoleService.updateSysRole(sysRole);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //角色添加
    @PostMapping("saveSysRole")
    public Result saveSysRole(@RequestBody SysRole sysRole){
        sysRoleService.saveSysRole(sysRole);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //根据角色名称进行条件分页查询
    @PostMapping("findByPage/{current}/{limit}")
    public Result findByPage(@PathVariable("current") Integer current,
                             @PathVariable("limit") Integer limit,
                             @RequestBody SysRoleDto sysRoleDto){

        //pageHelper插件
        PageInfo<SysRole> pageInfo = sysRoleService.findByPage(current,limit,sysRoleDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);

    }
}
