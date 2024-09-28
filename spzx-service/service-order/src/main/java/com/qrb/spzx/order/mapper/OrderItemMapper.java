package com.qrb.spzx.order.mapper;

import com.qrb.spzx.model.entity.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    //5.添加数据到order_item表
    void save(OrderItem orderItem);

    //根据每个订单的id查询所有订单项
    List<OrderItem> findByOrderId(Long id);
}
