package com.qrb.spzx.cart.service;

import com.qrb.spzx.model.entity.h5.CartInfo;

import java.util.List;

public interface CartService {
    //添加购物车
    void addToCart(Long skuId, Integer skuNum);

    //查询购物车
    List<CartInfo> getCartList();

    //删除购物车商品
    void deleteCart(Long skuId);

    //更新商品选中状态
    void checkCart(Long skuId, Integer isChecked);

    //购物车的商品全选全不选
    void allCheckCart(Integer isChecked);

    //清空购物车
    void clearCart();

    //远程调用 ： 用于获取购物车中选中的商品列表
    List<CartInfo> getAllCkecked();

    //远程调用 ： 删除Redis中已经下单的商品
    void deleteChecked();
}
