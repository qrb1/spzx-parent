package com.qrb.spzx.order.controller;


import com.github.pagehelper.PageInfo;
import com.qrb.spzx.model.dto.h5.OrderInfoDto;
import com.qrb.spzx.model.entity.order.OrderInfo;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.model.vo.h5.TradeVo;
import com.qrb.spzx.order.service.OrderInfoService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/order/orderInfo")
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;

    //远程调用：更新订单状态
    @GetMapping("auth/updateOrderStatusPayed/{orderNo}/{orderStatus}")
    public Result updateOrderStatus(@PathVariable(value = "orderNo") String orderNo,
                                    @PathVariable(value = "orderStatus") Integer orderStatus) {
        orderInfoService.updateOrderStatus(orderNo , orderStatus);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    //远程调用：根据订单编号获取订单信息
    @GetMapping("auth/getOrderInfoByOrderNo/{orderNo}")
    public OrderInfo getOrderInfoByOrderNo(@PathVariable("orderNo") String orderNo) {
        OrderInfo orderInfo = orderInfoService.getByOrderNo(orderNo) ;
        return orderInfo;
    }

    //我的订单接口（全部订单、代付款、待收货、待评价、已取消）
    //orderStatus默认是没有状态，也就是查全部。如果有状态就去查对应的数据：代付款、待收货、待评价、已取消
    @GetMapping("auth/{page}/{limit}")
    public Result list( @PathVariable("page") Integer page,
                        @PathVariable("limit") Integer limit,
                        @RequestParam(required = false, defaultValue = "") Integer orderStatus){
        PageInfo<OrderInfo> pageInfo = orderInfoService.findOrderPage(page,limit,orderStatus);
        return Result.build(pageInfo,ResultCodeEnum.SUCCESS);
    }

    //立即购买
    @GetMapping("auth/buy/{skuId}")
    public Result buy(@PathVariable("skuId") Long skuId){
        TradeVo tradeVo = orderInfoService.buy(skuId);
        return Result.build(tradeVo,ResultCodeEnum.SUCCESS);
    }

    //根据订单id查询订单详情
    @GetMapping("auth/{orderId}")
    public Result getOrderInfo(@PathVariable("orderId") Long orderId){
        OrderInfo orderInfo = orderInfoService.getOrderInfo(orderId);
        return Result.build(orderInfo,ResultCodeEnum.SUCCESS);
    }

    //下单
    @PostMapping("auth/submitOrder")
    public Result submitOrder(@RequestBody OrderInfoDto orderInfoDto) {
        Long orderId = orderInfoService.submitOrder(orderInfoDto);
        return Result.build(orderId,ResultCodeEnum.SUCCESS);
    }

    //结算 ： 需要远程调用 用于获取购物车中选中的商品列表 这个接口
    @GetMapping("auth/trade")
    public Result trade() {
        TradeVo tradeVo = orderInfoService.getTrade();
        return Result.build(tradeVo, ResultCodeEnum.SUCCESS);
    }
}
