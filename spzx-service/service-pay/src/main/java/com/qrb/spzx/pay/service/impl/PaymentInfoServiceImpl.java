package com.qrb.spzx.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.qrb.spzx.feign.order.OrderFeignClient;
import com.qrb.spzx.feign.product.ProductFeignClient;
import com.qrb.spzx.model.dto.product.SkuSaleDto;
import com.qrb.spzx.model.entity.order.OrderInfo;
import com.qrb.spzx.model.entity.order.OrderItem;
import com.qrb.spzx.model.entity.pay.PaymentInfo;
import com.qrb.spzx.pay.mapper.PaymentInfoMapper;
import com.qrb.spzx.pay.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    //保存支付记录
    @Override
    public PaymentInfo savePaymentInfo(String orderNo) {
        //根据订单编号查询支付记录
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(orderNo);
        //判断支付记录是否存在
        if(paymentInfo == null){
            //如果不存在则远程调用获取订单信息
            OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(orderNo);
            //封装到paymentInfo中
            paymentInfo = new PaymentInfo();
            paymentInfo.setUserId(orderInfo.getUserId());
            paymentInfo.setPayType(orderInfo.getPayType());
            String content = "";
            for(OrderItem item : orderInfo.getOrderItemList()) {
                content += item.getSkuName() + " ";
            }
            paymentInfo.setContent(content);
            paymentInfo.setAmount(orderInfo.getTotalAmount());
            paymentInfo.setOrderNo(orderNo);
            paymentInfo.setPaymentStatus(0);
            //进行保存到数据库
            paymentInfoMapper.save(paymentInfo);

        }
        return paymentInfo;
    }

    // 有上面的两个词表示正常的支付成功，我们应该更新交易记录状态
    @Override
    public void updatePaymentStatus(Map<String, String> map,Integer payType) {
        // 根据订单编号查询支付记录信息
        PaymentInfo paymentInfo = paymentInfoMapper.getByOrderNo(map.get("out_trade_no"));
        //如果支付记录已经完成，不需要更新
        if (paymentInfo.getPaymentStatus() == 1) {
            return;
        }
        //如果没有，则更新支付信息
        paymentInfo.setPaymentStatus(1);
        paymentInfo.setOutTradeNo(map.get("trade_no"));
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(JSON.toJSONString(map));
        paymentInfoMapper.updateById(paymentInfo);

        //更新订单状态
        orderFeignClient.updateOrderStatus(paymentInfo.getOrderNo() , payType) ;

        //更新sku商品销量
        //远程调用：根据skuId查询sku信息
        OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(paymentInfo.getOrderNo());
        //遍历，更新sku商品销量
        List<SkuSaleDto> skuSaleDtoList = orderInfo.getOrderItemList().stream().map(item -> {
            SkuSaleDto skuSaleDto = new SkuSaleDto();
            skuSaleDto.setSkuId(item.getSkuId());
            skuSaleDto.setNum(item.getSkuNum());
            return skuSaleDto;
        }).collect(Collectors.toList());
        productFeignClient.updateSkuSaleNum(skuSaleDtoList) ;
    }
}
