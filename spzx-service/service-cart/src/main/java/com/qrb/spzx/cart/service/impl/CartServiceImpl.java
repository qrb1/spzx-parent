package com.qrb.spzx.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.qrb.spzx.cart.service.CartService;
import com.qrb.spzx.feign.product.ProductFeignClient;
import com.qrb.spzx.model.entity.h5.CartInfo;
import com.qrb.spzx.model.entity.product.ProductSku;
import com.qrb.spzx.model.entity.user.UserInfo;
import com.qrb.spzx.utils.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate<String , String> redisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ProductFeignClient productFeignClient;

    //添加购物车
    @Override
    public void addToCart(Long skuId, Integer skuNum) {
        //获取当前登录的用户的id
        String token = request.getHeader("token");
        String userInfoString = redisTemplate.opsForValue().get("user:spzx:" + token);
        UserInfo userInfo = JSON.parseObject(userInfoString, UserInfo.class);
        Long id = userInfo.getId();
        String userId = id.toString();
        String cartKey = "user:cart:" + userId;
        //然后根据id和skuId去Redis中的购物车数据
        Object cartInfoObj = redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId));
        //如果购物车中存在商品，则相加
        CartInfo cartInfo = null;
        if(cartInfoObj != null){
            cartInfo = JSON.parseObject(cartInfoObj.toString(), CartInfo.class);
            cartInfo.setSkuNum(cartInfo.getSkuNum()+skuNum);
            cartInfo.setIsChecked(1);
            cartInfo.setUpdateTime(new Date());
        }else {
            //如果不存在，则将商品添加到购物车 //远程调用 nacos+openFeign
            cartInfo = new CartInfo();
            //远程调用：根据skuId获取sku的信息
            ProductSku productSku = productFeignClient.getBySkuId(skuId);
            cartInfo.setCartPrice(productSku.getSalePrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setSkuId(skuId);
            cartInfo.setUserId(id);
            cartInfo.setImgUrl(productSku.getThumbImg());
            cartInfo.setSkuName(productSku.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
        }
        //添加到Redis里面
        redisTemplate.opsForHash().put(cartKey , String.valueOf(skuId) , JSON.toJSONString(cartInfo));

    }

    //查询购物车
    @Override
    public List<CartInfo> getCartList() {
        //获取当前登录的用户的id
        String token = request.getHeader("token");
        String userInfoString = redisTemplate.opsForValue().get("user:spzx:" + token);
        UserInfo userInfo = JSON.parseObject(userInfoString, UserInfo.class);
        Long id = userInfo.getId();
        String userId = id.toString();
        String cartKey = "user:cart:" + userId;
        //根据用户id获取Redis中的value
        List<Object> values = redisTemplate.opsForHash().values(cartKey);
        if(!CollectionUtils.isEmpty(values)){
            //类型转换，封装数据
            List<CartInfo> cartInfoList = values
                    .stream()
                    .map(cartInfoObj -> JSON.parseObject(cartInfoObj.toString(), CartInfo.class))
                    .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()))
                    .collect(Collectors.toList());
            return cartInfoList;
        }
        return new ArrayList<>();
    }

    //删除购物车商品
    @Override
    public void deleteCart(Long skuId) {
        //获取当前登录的用户的id
        String token = request.getHeader("token");
        String userInfoString = redisTemplate.opsForValue().get("user:spzx:" + token);
        UserInfo userInfo = JSON.parseObject(userInfoString, UserInfo.class);
        Long id = userInfo.getId();
        String userId = id.toString();
        String cartKey = "user:cart:" + userId;
        redisTemplate.opsForHash().delete(cartKey,String.valueOf(skuId));
    }

    //更新商品选中状态
    @Override
    public void checkCart(Long skuId, Integer isChecked) {
        //获取当前登录的用户的id
        String token = request.getHeader("token");
        String userInfoString = redisTemplate.opsForValue().get("user:spzx:" + token);
        UserInfo userInfo = JSON.parseObject(userInfoString, UserInfo.class);
        Long id = userInfo.getId();
        String userId = id.toString();
        String cartKey = "user:cart:" + userId;
        //通过用户id(cartKey)判断是否有field
        Boolean hasKey = redisTemplate.opsForHash().hasKey(cartKey, String.valueOf(skuId));
        if(hasKey){
            //有则cartKey+skuId--->判断是否有value
            String cartString = redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId)).toString();
            //然后修改选中的状态，然后重新放入到Redis中
            CartInfo cartInfo = JSON.parseObject(cartString, CartInfo.class);
            cartInfo.setIsChecked(isChecked);
            redisTemplate.opsForHash().put(cartKey,String.valueOf(skuId),JSON.toJSONString(cartInfo));
        }
    }

    //购物车的商品全选全不选
    @Override
    public void allCheckCart(Integer isChecked) {
        //获取当前登录的用户的id
        String token = request.getHeader("token");
        String userInfoString = redisTemplate.opsForValue().get("user:spzx:" + token);
        UserInfo userInfo = JSON.parseObject(userInfoString, UserInfo.class);
        Long id = userInfo.getId();
        String userId = id.toString();
        String cartKey = "user:cart:" + userId;
        //根据用户id去获取商品的value
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);
        if(!CollectionUtils.isEmpty(objectList)) {
            //List<Object> ---->List<CartInfo>
            objectList.stream().map(cartInfoJSON -> {
                CartInfo cartInfo = JSON.parseObject(cartInfoJSON.toString(), CartInfo.class);
                cartInfo.setIsChecked(isChecked);
                return cartInfo ;
                //遍历，修改每一个的状态，并重新放回到Redis中
            }).forEach(cartInfo -> redisTemplate.opsForHash().put(cartKey , String.valueOf(cartInfo.getSkuId()) , JSON.toJSONString(cartInfo)));

        }
    }

    //清空购物车
    @Override
    public void clearCart() {
        //获取当前登录的用户的id
        String token = request.getHeader("token");
        String userInfoString = redisTemplate.opsForValue().get("user:spzx:" + token);
        UserInfo userInfo = JSON.parseObject(userInfoString, UserInfo.class);
        Long id = userInfo.getId();
        String userId = id.toString();
        String cartKey = "user:cart:" + userId;
        //根据id去删除Redis中的数据
        redisTemplate.delete(cartKey);
    }

    //远程调用 ： 用于获取购物车中选中的商品列表
    @Override
    public List<CartInfo> getAllCkecked() {
        //获取当前登录的用户的id
        String token = request.getHeader("token");
        String userInfoString = redisTemplate.opsForValue().get("user:spzx:" + token);
        UserInfo userInfo = JSON.parseObject(userInfoString, UserInfo.class);
        Long id = userInfo.getId();
        String userId = id.toString();
        String cartKey = "user:cart:" + userId;
        //根据用户id去获取商品的value
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);
        if(!CollectionUtils.isEmpty(objectList)) {
            //List<Object> ---->List<CartInfo>
            List<CartInfo> cartInfoList = objectList
                                            .stream()
                                            .map(cartInfoJSON ->
                                                    JSON.parseObject(cartInfoJSON.toString(), CartInfo.class))
                                            .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                                            .collect(Collectors.toList());
            return cartInfoList;
        }
        return new ArrayList<>();
    }

    //远程调用 ： 删除Redis中已经下单的商品
    @Override
    public void deleteChecked() {
        //获取当前登录的用户的id
        String token = request.getHeader("token");
        String userInfoString = redisTemplate.opsForValue().get("user:spzx:" + token);
        UserInfo userInfo = JSON.parseObject(userInfoString, UserInfo.class);
        Long id = userInfo.getId();
        String userId = id.toString();
        String cartKey = "user:cart:" + userId;

        //根据key获取Redis中所有value
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);
        //删除商品
        if(!CollectionUtils.isEmpty(objectList)) {
            objectList
                    .stream()
                    .map(cartInfoJSON
                            -> JSON.parseObject(cartInfoJSON.toString(), CartInfo.class))
                    .filter(cartInfo
                            -> cartInfo.getIsChecked() == 1)
                    .forEach(cartInfo
                            -> redisTemplate.opsForHash().delete(cartKey , String.valueOf(cartInfo.getSkuId())));
        }
    }
}
