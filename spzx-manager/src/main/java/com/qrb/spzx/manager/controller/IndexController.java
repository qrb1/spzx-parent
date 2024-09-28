package com.qrb.spzx.manager.controller;

import com.qrb.spzx.manager.service.SysMenuService;
import com.qrb.spzx.manager.service.SysUserService;
import com.qrb.spzx.manager.service.ValidateCodeService;
import com.qrb.spzx.model.dto.system.LoginDto;
import com.qrb.spzx.model.entity.system.SysUser;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.model.vo.system.LoginVo;
import com.qrb.spzx.model.vo.system.SysMenuVo;
import com.qrb.spzx.model.vo.system.ValidateCodeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="用户接口")
/*  allowCredentials:是否允许在跨域的情况下传递Cookie，true为允许
    originPatterns:允许哪些域的来源的请求，*为允许
    allowedHeaders:允许哪些请求的请求头，*为允许
 */
@CrossOrigin(allowCredentials = "true" , originPatterns = "*" , allowedHeaders = "*")
@RestController
@RequestMapping(value = "/admin/system/index")
public class IndexController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private SysMenuService sysMenuService;

    //TODO 登录校验暂不做

    //实现动态菜单
    @GetMapping("menus")
    public Result menus(HttpServletRequest request){
        List<SysMenuVo> list = sysMenuService.findMenuByUserId(request);
        return Result.build(list,ResultCodeEnum.SUCCESS);
    }

    //用户退出
    @GetMapping(value = "logout")
    public Result logout(HttpServletRequest request){
        String token = request.getHeader("token");
        sysUserService.logout(token);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //获取登录的用户信息
    @GetMapping(value = "getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        //1 从请求头里面获取token
        String token = request.getHeader("token");
        //2 将token去redis中获取用户信息
        SysUser sysUser = sysUserService.getUserInfo(token);
        //3 将用户信息进行返回
        return Result.build(sysUser,ResultCodeEnum.SUCCESS);
    }

    //生成图片验证码
    //ValidateCodeVo 封装要返回给前端的验证码的key 和 value
    @GetMapping("generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo,ResultCodeEnum.SUCCESS);
    }

    //用户登录
    //LoginDto 实体类封装前端传给后端的数据
    //LoginVo 实体类封装后端传给前端的数据
    @Operation(summary = "登录的方法")
    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto){
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }

}
