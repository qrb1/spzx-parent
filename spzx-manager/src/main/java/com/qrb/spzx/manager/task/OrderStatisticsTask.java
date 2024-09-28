package com.qrb.spzx.manager.task;


import cn.hutool.core.date.DateUtil;
import com.qrb.spzx.manager.mapper.OrderInfoMapper;
import com.qrb.spzx.manager.mapper.OrderStatisticsMapper;
import com.qrb.spzx.model.entity.order.OrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderStatisticsTask {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    //每天的凌晨2点查询前一天的日期并统计数据，并把数据添加到统计结果表里面
    @Scheduled(cron = "0 0 2 * * ?")
    //测试：@Scheduled(cron = "0/10 * * * * ? ")
    public void orderTotalAmountStatistics(){
        //获取前一天的日期
        String create_date = DateUtil.offsetDay(new Date(), -1).toString("yyyy-MM-dd");
        //根据日期查询统计数据
        OrderStatistics orderStatistics =orderInfoMapper.selectOrderStatistics(create_date);
        //将统计的数据添加到统计结果表里面
        if(orderStatistics != null){
            orderStatisticsMapper.insert(orderStatistics);
        }
    }
}
