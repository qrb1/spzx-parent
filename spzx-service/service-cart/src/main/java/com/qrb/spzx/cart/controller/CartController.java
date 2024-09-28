package com.qrb.spzx.cart.controller;


import com.qrb.spzx.cart.service.CartService;
import com.qrb.spzx.model.entity.h5.CartInfo;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    //远程调用 ： 删除Redis中已经下单的商品
    @GetMapping(value = "/auth/deleteChecked")
    public Result deleteChecked() {
        cartService.deleteChecked() ;
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    //远程调用 ： 用于获取购物车中选中的商品列表
    @GetMapping(value = "/auth/getAllCkecked")
    public List<CartInfo> getAllCkecked() {
        List<CartInfo> cartInfoList = cartService.getAllCkecked() ;
        return cartInfoList;
    }

    //清空购物车
    @GetMapping("/auth/clearCart")
    public Result clearCart(){
        cartService.clearCart();
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //购物车的商品全选全不选
    @GetMapping("/auth/allCheckCart/{isChecked}")
    public Result allCheckCart(@PathVariable(value = "isChecked") Integer isChecked){
        cartService.allCheckCart(isChecked);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //更新商品选中状态
    @GetMapping("/auth/checkCart/{skuId}/{isChecked}")
    public Result checkCart(@PathVariable(value = "skuId") Long skuId,
                            @PathVariable(value = "isChecked") Integer isChecked) {
        cartService.checkCart(skuId,isChecked);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //删除购物车商品
    @DeleteMapping("auth/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable("skuId") Long skuId) {
        cartService.deleteCart(skuId);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //添加购物车
    @GetMapping("auth/addToCart/{skuId}/{skuNum}")
    public Result addToCart(@PathVariable("skuId") Long skuId,
                            @PathVariable("skuNum") Integer skuNum){
        cartService.addToCart(skuId,skuNum);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //查询购物车
    @GetMapping("auth/cartList")
    public Result cartList() {
        List<CartInfo> cartInfoList = cartService.getCartList();
        return Result.build(cartInfoList, ResultCodeEnum.SUCCESS);
    }
}
