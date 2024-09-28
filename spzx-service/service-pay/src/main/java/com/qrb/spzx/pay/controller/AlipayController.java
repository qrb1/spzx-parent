package com.qrb.spzx.pay.controller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.qrb.spzx.model.vo.common.Result;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.pay.service.AlipayService;
import com.qrb.spzx.pay.service.PaymentInfoService;
import com.qrb.spzx.pay.utils.AlipayProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/order/alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private PaymentInfoService paymentInfoService;

    //支付宝异步回调
    @RequestMapping("callback/notify")
    @ResponseBody
    public String alipayNotify(@RequestParam Map<String, String> paramMap, HttpServletRequest request) {
        boolean signVerified = false; //调用SDK验证签名,验证是否是合法性
        try {
            signVerified = AlipaySignature.rsaCheckV1(paramMap, alipayProperties.getAlipayPublicKey(), AlipayProperties.charset, AlipayProperties.sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        // 交易状态
        String trade_status = paramMap.get("trade_status");
        // 如果是合法性
        if (signVerified) {
            if ("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)) {
                // 有上面的两个词表示正常的支付成功，我们应该更新交易记录状态
                paymentInfoService.updatePaymentStatus(paramMap,2);
                return "success";
            }
        } else {
            return "failure";
        }
        return "failure";

    }

    //支付宝下单
    @GetMapping("submitAlipay/{orderNo}")
    @ResponseBody
    public Result submitAlipay(@PathVariable("orderNo") String orderNo) {
        String form = alipayService.submitAlipay(orderNo);
        return Result.build(form, ResultCodeEnum.SUCCESS);
    }
}
