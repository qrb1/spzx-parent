package com.qrb.spzx.feign.cart;


import com.qrb.spzx.model.entity.h5.CartInfo;
import com.qrb.spzx.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "service-cart")
public interface CartFeignClient {

    //用于获取购物车中选中的商品列表
    @GetMapping("/api/order/cart/auth/getAllCkecked")
    public List<CartInfo> getAllCkecked() ;

    //删除Redis中已经下单的商品
    @GetMapping("/api/order/cart/auth/deleteChecked")
    public Result deleteChecked() ;

}
