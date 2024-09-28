package com.qrb.spzx.manager.service;

import com.qrb.spzx.model.dto.order.OrderStatisticsDto;
import com.qrb.spzx.model.vo.order.OrderStatisticsVo;

public interface OrderInfoService {
    //查询统计的数据
    OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto);
}
