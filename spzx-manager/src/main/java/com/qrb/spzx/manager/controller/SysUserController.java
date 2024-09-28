package com.qrb.spzx.manager.controller;

import com.github.pagehelper.PageInfo;
import com.qrb.spzx.manager.service.SysUserService;
import com.qrb.spzx.model.dto.system.AssginRoleDto;
import com.qrb.spzx.model.dto.system.SysUserDto;
import com.qrb.spzx.model.entity.system.SysUser;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(allowCredentials = "true" , originPatterns = "*" , allowedHeaders = "*")
@RestController
@RequestMapping(value = "/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    //为用户分配角色：将选中的角色保存到数据库中
    @PostMapping("doAssgin")
    public Result doAssgin(@RequestBody AssginRoleDto assginRoleDto){
        sysUserService.doAssgin(assginRoleDto);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }


    //用户管理：条件分页查询
    @PostMapping("findByPage/{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable("pageNum") Integer pageNum,
                             @PathVariable("pageSize") Integer pageSize,
                             @RequestBody SysUserDto sysUserdto){
        PageInfo<SysUser> pageInfo = sysUserService.findByPage(pageNum,pageSize,sysUserdto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //用户管理：用户添加
    @PostMapping("saveSysUser")
    public Result saveSysUser(@RequestBody SysUser sysUser){
        sysUserService.saveSysUser(sysUser);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //用户管理：用户修改
    @PutMapping("updateSysUser")
    public Result updateSysUser(@RequestBody SysUser sysUser){
        sysUserService.updateSysUser(sysUser);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //用户管理：用户删除
    @DeleteMapping("deleteById/{userId}")
    public Result deleteById(@PathVariable("userId") Long userId){
        sysUserService.deleteById(userId);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

}
