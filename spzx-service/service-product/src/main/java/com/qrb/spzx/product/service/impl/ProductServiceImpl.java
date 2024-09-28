package com.qrb.spzx.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qrb.spzx.model.dto.h5.ProductSkuDto;
import com.qrb.spzx.model.dto.product.SkuSaleDto;
import com.qrb.spzx.model.entity.product.Product;
import com.qrb.spzx.model.entity.product.ProductDetails;
import com.qrb.spzx.model.entity.product.ProductSku;
import com.qrb.spzx.model.vo.h5.ProductItemVo;
import com.qrb.spzx.product.mapper.ProductDetailsMapper;
import com.qrb.spzx.product.mapper.ProductMapper;
import com.qrb.spzx.product.mapper.ProductSkuMapper;
import com.qrb.spzx.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    //2.根据畅销对商品进行排序，只获取前10条记录
    @Override
    public List<ProductSku> selectProductSkuBySale() {
        return productSkuMapper.selectProductSkuBySale();
    }

    //条件分页查询
    @Override
    public PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto) {
        //设置分页
        PageHelper.startPage(page,limit);
        //条件查询
        List<ProductSku> list = productSkuMapper.findByPage(productSkuDto);
        //封装数据
        PageInfo<ProductSku> productSkuPageInfo = new PageInfo<>(list);
        //返回
        return productSkuPageInfo;
    }

    //根据商品id查询商品详情
    @Override
    public ProductItemVo item(Long skuId) {
        //创建对象，用于封装最终的数据
        ProductItemVo productItemVo = new ProductItemVo();
        //根据skuId去获取sku信息
        ProductSku productSku = productSkuMapper.getById(skuId);
        //通过获取的sku信息获取productId,获取商品信息
        Long productId = productSku.getProductId();
        Product product = productMapper.getById(productId);
        //根据productId获取商品详细信息
        ProductDetails productDetails = productDetailsMapper.getByProductId(productId);
        //封装map集合,封装商品的规格和对应的skuId
        Map<String, Object> skuSpecValueMap = new HashMap<>();
        //根据商品id获取商品所有sku列表
        List<ProductSku> productSkuList = productSkuMapper.findByProductId(productId);
        productSkuList.forEach(item ->{
            skuSpecValueMap.put(item.getSkuSpec(),item.getId());
        });
        //封装最终数据
        productItemVo.setProductSku(productSku);
        productItemVo.setProduct(product);
        productItemVo.setDetailsImageUrlList(Arrays.asList(productDetails.getImageUrls().split(",")));
        productItemVo.setSliderUrlList(Arrays.asList(product.getSliderUrls().split(",")));
        productItemVo.setSpecValueList(JSON.parseArray(product.getSpecValue()));
        productItemVo.setSkuSpecValueMap(skuSpecValueMap);
        return productItemVo;
    }

    //远程调用：根据skuId查询sku信息
    @Override
    public ProductSku getBySkuId(Long skuId) {
        ProductSku byId = productSkuMapper.getById(skuId);
        return byId;
    }

    //更新商品sku销量
    @Override
    public Boolean updateSkuSaleNum(List<SkuSaleDto> skuSaleDtoList) {
        if(!CollectionUtils.isEmpty(skuSaleDtoList)) {
            for(SkuSaleDto skuSaleDto : skuSaleDtoList) {
                productSkuMapper.updateSale(skuSaleDto.getSkuId(), skuSaleDto.getNum());
            }
        }
        return true;
    }
}
