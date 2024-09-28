package com.qrb.spzx.order.service;

import com.github.pagehelper.PageInfo;
import com.qrb.spzx.model.dto.h5.OrderInfoDto;
import com.qrb.spzx.model.entity.order.OrderInfo;
import com.qrb.spzx.model.vo.h5.TradeVo;

public interface OrderInfoService {
    //结算 ： 需要远程调用 用于获取购物车中选中的商品列表 这个接口
    TradeVo getTrade();

    //下单
    Long submitOrder(OrderInfoDto orderInfoDto);

    //根据订单id查询订单详情
    OrderInfo getOrderInfo(Long orderId);

    //立即购买
    TradeVo buy(Long skuId);

    //我的订单接口（全部订单、代付款、待收货、待评价、已取消）
    PageInfo<OrderInfo> findOrderPage(Integer page, Integer limit, Integer orderStatus);

    //远程调用：根据订单编号获取订单信息
    OrderInfo getByOrderNo(String orderNo);

    //远程调用：更新订单状态
    void updateOrderStatus(String orderNo, Integer orderStatus);
}
