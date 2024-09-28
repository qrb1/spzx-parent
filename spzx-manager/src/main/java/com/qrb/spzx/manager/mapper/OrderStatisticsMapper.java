package com.qrb.spzx.manager.mapper;


import com.qrb.spzx.model.dto.order.OrderStatisticsDto;
import com.qrb.spzx.model.entity.order.OrderStatistics;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderStatisticsMapper {
    //将统计的数据添加到统计结果表里面
    void insert(OrderStatistics orderStatistics);

    //根据dto查询数据
    List<OrderStatistics> selectList(OrderStatisticsDto orderStatisticsDto);
}
