package com.qrb.spzx.common.exception;

import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice //controller增强器
public class GlobalExceptionHandler {

    //全局异常
    @ExceptionHandler(Exception.class) //异常处理器
    @ResponseBody //让方法返回json格式的数据
    public Result error(){
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    //自定义异常处理
    @ExceptionHandler(GuiguException.class) //异常处理器
    @ResponseBody //让方法返回json格式的数据
    public Result error(GuiguException e){
        return Result.build(null, e.getResultCodeEnum());
    }
}
