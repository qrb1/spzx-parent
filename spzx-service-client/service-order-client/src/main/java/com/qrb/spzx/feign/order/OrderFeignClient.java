package com.qrb.spzx.feign.order;

import com.qrb.spzx.model.entity.order.OrderInfo;
import com.qrb.spzx.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-order")
public interface OrderFeignClient {

    //远程调用：根据订单编号获取订单信息
    @GetMapping("/api/order/orderInfo/auth/getOrderInfoByOrderNo/{orderNo}")
    public OrderInfo getOrderInfoByOrderNo(@PathVariable String orderNo) ;

    //远程调用：更新订单状态
    @GetMapping("/api/order/orderInfo/auth/updateOrderStatusPayed/{orderNo}/{orderStatus}")
    public Result updateOrderStatus(@PathVariable(value = "orderNo") String orderNo
            , @PathVariable(value = "orderStatus") Integer orderStatus) ;

}
