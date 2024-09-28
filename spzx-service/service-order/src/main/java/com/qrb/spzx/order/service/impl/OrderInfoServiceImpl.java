package com.qrb.spzx.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qrb.spzx.common.exception.GuiguException;
import com.qrb.spzx.feign.cart.CartFeignClient;
import com.qrb.spzx.feign.product.ProductFeignClient;
import com.qrb.spzx.feign.user.UserFeignClient;
import com.qrb.spzx.model.dto.h5.OrderInfoDto;
import com.qrb.spzx.model.entity.h5.CartInfo;
import com.qrb.spzx.model.entity.order.OrderInfo;
import com.qrb.spzx.model.entity.order.OrderItem;
import com.qrb.spzx.model.entity.order.OrderLog;
import com.qrb.spzx.model.entity.product.ProductSku;
import com.qrb.spzx.model.entity.user.UserAddress;
import com.qrb.spzx.model.entity.user.UserInfo;
import com.qrb.spzx.model.vo.common.ResultCodeEnum;
import com.qrb.spzx.model.vo.h5.TradeVo;
import com.qrb.spzx.order.mapper.OrderInfoMapper;
import com.qrb.spzx.order.mapper.OrderItemMapper;
import com.qrb.spzx.order.mapper.OrderLogMapper;
import com.qrb.spzx.order.service.OrderInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserFeignClient userFeignClient;

    //结算 ： 需要远程调用 用于获取购物车中选中的商品列表 这个接口
    @Override
    public TradeVo getTrade() {
        //远程调用 用于获取购物车中选中的商品列表 这个接口
        List<CartInfo> cartInfoList = cartFeignClient.getAllCkecked();
        // 将购物项数据转换成功订单明细数据
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartInfo cartInfo : cartInfoList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setThumbImg(cartInfo.getImgUrl());
            orderItemList.add(orderItem);
        }
        // 计算总金额
        BigDecimal totalAmount = new BigDecimal(0);
        for(OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        TradeVo tradeVo = new TradeVo();
        tradeVo.setTotalAmount(totalAmount);
        tradeVo.setOrderItemList(orderItemList);
        return tradeVo;
    }

    //下单
    @Override
    public Long submitOrder(OrderInfoDto orderInfoDto) {
        //1.从orderInfoDto获取所有的订单项 List<OrderItem>
        List<OrderItem> orderItemList = orderInfoDto.getOrderItemList();
        //2.判断List<OrderItem>是否为null，是抛出异常
        if (CollectionUtils.isEmpty(orderItemList)) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        //3.判断库存是否充足 -------------远程调用
        for (OrderItem orderItem : orderItemList) {
            //获取商品信息
            ProductSku productSku = productFeignClient.getBySkuId(orderItem.getSkuId());
            if(null == productSku) {
                throw new GuiguException(ResultCodeEnum.DATA_ERROR);
            }
            //校验库存
            if(orderItem.getSkuNum().intValue() > productSku.getStockNum().intValue()) {//购买的数量>库存数量
                throw new GuiguException(ResultCodeEnum.STOCK_LESS);
            }
        }
        //4.添加数据到order_info表-------------远程调用
        String token = request.getHeader("token");
        String userString = redisTemplate.opsForValue().get("user:spzx:" + token);
        UserInfo userInfo = JSON.parseObject(userString, UserInfo.class);
        OrderInfo orderInfo = new OrderInfo();
        //订单编号
        orderInfo.setOrderNo(String.valueOf(System.currentTimeMillis()));
        //用户id
        orderInfo.setUserId(userInfo.getId());
        //用户昵称
        orderInfo.setNickName(userInfo.getNickName());
        //用户收货地址信息
        UserAddress userAddress = userFeignClient.getUserAddress(orderInfoDto.getUserAddressId());
        orderInfo.setReceiverName(userAddress.getName());
        orderInfo.setReceiverPhone(userAddress.getPhone());
        orderInfo.setReceiverTagName(userAddress.getTagName());
        orderInfo.setReceiverProvince(userAddress.getProvinceCode());
        orderInfo.setReceiverCity(userAddress.getCityCode());
        orderInfo.setReceiverDistrict(userAddress.getDistrictCode());
        orderInfo.setReceiverAddress(userAddress.getFullAddress());
        //订单金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setCouponAmount(new BigDecimal(0));
        orderInfo.setOriginalTotalAmount(totalAmount);
        orderInfo.setFeightFee(orderInfoDto.getFeightFee());
        orderInfo.setPayType(2);
        orderInfo.setOrderStatus(0);
        orderInfoMapper.save(orderInfo);

        //5.添加数据到order_item表
        for (OrderItem orderItem : orderItemList) {
            //设置对应的订单id
            orderItem.setOrderId(orderInfo.getId());
            orderItemMapper.save(orderItem);
        }

        //6.添加数据到order_log表
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(0);
        orderLog.setNote("提交订单");
        orderLogMapper.save(orderLog);

        //7.远程调用:  service-cart微服务接口清空 Redis中已经下单的购物车数据
        cartFeignClient.deleteChecked();
        //8.返回订单id
        return orderInfo.getId();
    }

    //根据订单id查询订单详情
    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return orderInfoMapper.getById(orderId);
    }

    //立即购买
    @Override
    public TradeVo buy(Long skuId) {
        // 查询商品
        ProductSku productSku = productFeignClient.getBySkuId(skuId);
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setSkuId(skuId);
        orderItem.setSkuName(productSku.getSkuName());
        orderItem.setSkuNum(1);
        orderItem.setSkuPrice(productSku.getSalePrice());
        orderItem.setThumbImg(productSku.getThumbImg());
        orderItemList.add(orderItem);

        // 计算总金额
        BigDecimal totalAmount = productSku.getSalePrice();
        TradeVo tradeVo = new TradeVo();
        tradeVo.setTotalAmount(totalAmount);
        tradeVo.setOrderItemList(orderItemList);
        return tradeVo;
    }

    //我的订单接口（全部订单、代付款、待收货、待评价、已取消）
    @Override
    public PageInfo<OrderInfo> findOrderPage(Integer page, Integer limit, Integer orderStatus) {
        PageHelper.startPage(page,limit);
        //查询订单
        //获取当前登录的用户的id
        String token = request.getHeader("token");
        String userInfoString = redisTemplate.opsForValue().get("user:spzx:" + token);
        UserInfo userInfo = JSON.parseObject(userInfoString, UserInfo.class);
        Long userId = userInfo.getId();
        //根据用户id获取全部订单
        List<OrderInfo> orderInfoList = orderInfoMapper.findUserPage(userId,orderStatus);
        //遍历每个订单
        orderInfoList.forEach(orderInfo -> {
            //根据每个订单的id查询所有订单项
            List<OrderItem> orderItemList = orderItemMapper.findByOrderId(orderInfo.getId());
            orderInfo.setOrderItemList(orderItemList);
        });
        return new PageInfo<>(orderInfoList);
    }

    //远程调用：根据订单编号获取订单信息
    @Override
    public OrderInfo getByOrderNo(String orderNo) {
        OrderInfo orderInfo = orderInfoMapper.getByOrderNo(orderNo);
        List<OrderItem> orderItemList = orderItemMapper.findByOrderId(orderInfo.getId());
        orderInfo.setOrderItemList(orderItemList);
        return orderInfo;
    }

    //远程调用：更新订单状态
    @Override
    public void updateOrderStatus(String orderNo, Integer orderStatus) {
        // 更新订单状态
        OrderInfo orderInfo = orderInfoMapper.getByOrderNo(orderNo);
        orderInfo.setOrderStatus(1);
        orderInfo.setPayType(orderStatus);
        orderInfo.setPaymentTime(new Date());
        orderInfoMapper.updateById(orderInfo);

        // 记录日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(1);
        orderLog.setNote("支付宝支付成功");
        orderLogMapper.save(orderLog);

    }
}
