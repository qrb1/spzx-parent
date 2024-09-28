package com.qrb.spzx.manager.service.impl;

import cn.hutool.core.date.DateUtil;
import com.qrb.spzx.manager.mapper.OrderStatisticsMapper;
import com.qrb.spzx.manager.service.OrderInfoService;
import com.qrb.spzx.model.dto.order.OrderStatisticsDto;
import com.qrb.spzx.model.entity.order.OrderStatistics;
import com.qrb.spzx.model.vo.order.OrderStatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    //查询统计的数据
    @Override
    public OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {
        //根据dto查询数据
        List<OrderStatistics> orderStatisticsList= orderStatisticsMapper.selectList(orderStatisticsDto);
        //遍历获取所有日期，并封装到list集合中
        List<String> dateList = orderStatisticsList
                .stream()
                .map(orderStatistics ->
                        DateUtil.format(orderStatistics.getOrderDate(), "yyyy-MM-dd"))
                .collect(Collectors.toList());
        //遍历获取所有日期所对应的总金额，并封装到list集合中
        List<BigDecimal> decimalList = orderStatisticsList
                .stream()
                .map(orderStatistics -> orderStatistics.getTotalAmount())
                .collect(Collectors.toList());
        //将两个list集合封装到OrderStatisticsVo中进行返回
        OrderStatisticsVo orderStatisticsVo = new OrderStatisticsVo();
        orderStatisticsVo.setDateList(dateList);
        orderStatisticsVo.setAmountList(decimalList);
        return orderStatisticsVo;
    }
}
