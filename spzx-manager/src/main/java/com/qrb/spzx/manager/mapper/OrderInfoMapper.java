package com.qrb.spzx.manager.mapper;


import com.qrb.spzx.model.entity.order.OrderStatistics;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper {
    //根据日期查询统计数据
    OrderStatistics selectOrderStatistics(String create_date);
}
