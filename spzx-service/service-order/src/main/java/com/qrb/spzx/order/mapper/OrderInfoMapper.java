package com.qrb.spzx.order.mapper;

import com.qrb.spzx.model.entity.order.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderInfoMapper {
    //添加数据到order_info表
    void save(OrderInfo orderInfo);

    //根据订单id查询订单详情
    OrderInfo getById(Long orderId);

    //根据用户id获取全部订单
    List<OrderInfo> findUserPage(Long userId, Integer orderStatus);

    //远程调用：根据订单编号获取订单信息
    OrderInfo getByOrderNo(String orderNo);

    // 更新订单状态
    void updateById(OrderInfo orderInfo);
}
