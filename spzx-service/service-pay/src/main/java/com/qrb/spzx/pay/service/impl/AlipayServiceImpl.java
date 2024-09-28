package com.qrb.spzx.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.qrb.spzx.common.exception.GuiguException;
import com.qrb.spzx.feign.order.OrderFeignClient;
import com.qrb.spzx.model.entity.pay.PaymentInfo;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.pay.service.AlipayService;
import com.qrb.spzx.pay.service.PaymentInfoService;
import com.qrb.spzx.pay.utils.AlipayProperties;
import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private AlipayProperties alipayProperties ;



    //支付宝下单
    @Override
    public String submitAlipay(String orderNo) {

        //保存支付记录
        PaymentInfo paymentInfo = paymentInfoService.savePaymentInfo(orderNo);

        //创建API对应的request
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        // 同步回调 ，即返回支付成功的页面
        alipayRequest.setReturnUrl(alipayProperties.getReturnPaymentUrl());

        // 异步回调，即支付成功后会更新订单状态
        alipayRequest.setNotifyUrl(alipayProperties.getNotifyPaymentUrl());

        // 准备请求参数 ，声明一个map 集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no",paymentInfo.getOrderNo());
        map.put("product_code","QUICK_WAP_WAY");
        //map.put("total_amount",paymentInfo.getAmount());
        map.put("total_amount",new BigDecimal("0.01"));//支付金额
        map.put("subject",paymentInfo.getContent());
        alipayRequest.setBizContent(JSON.toJSONString(map));

        // 发送请求
        try {
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);
            if (response.isSuccess()) {
                String form = response.getBody();
                return form;
            } else {
                throw new GuiguException(ResultCodeEnum.DATA_ERROR);
            }
        }catch (AlipayApiException e){
            throw new RuntimeException(e);
        }
    }
}
