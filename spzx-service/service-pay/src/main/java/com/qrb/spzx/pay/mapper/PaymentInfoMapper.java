package com.qrb.spzx.pay.mapper;

import com.qrb.spzx.model.entity.pay.PaymentInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentInfoMapper {
    //根据订单编号查询支付记录
    PaymentInfo getByOrderNo(String orderNo);

    //进行保存到数据库
    void save(PaymentInfo paymentInfo);

    //如果没有，则更新支付信息
    void updateById(PaymentInfo paymentInfo);
}
