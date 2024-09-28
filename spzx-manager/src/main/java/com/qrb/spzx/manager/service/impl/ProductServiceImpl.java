package com.qrb.spzx.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qrb.spzx.manager.mapper.ProductDetailsMapper;
import com.qrb.spzx.manager.mapper.ProductMapper;
import com.qrb.spzx.manager.mapper.ProductSkuMapper;
import com.qrb.spzx.manager.service.ProductService;
import com.qrb.spzx.model.dto.product.ProductDto;
import com.qrb.spzx.model.entity.product.Product;
import com.qrb.spzx.model.entity.product.ProductDetails;
import com.qrb.spzx.model.entity.product.ProductSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    //列表条件分页查询
    @Override
    public PageInfo<Product> findByPage(Integer page, Integer limit, ProductDto productDto) {
        //设置分页
        PageHelper.startPage(page,limit);
        //条件查询
        List<Product>list = productMapper.findByPage(productDto);
        //封装数据
        PageInfo<Product> pageInfo = new PageInfo<>(list);
        //返回
        return pageInfo;
    }

    //商品管理：添加商品信息到数据库
    @Override
    public void save(Product product) {
        //保存商品的信息到product表
        product.setAuditStatus(0);
        product.setStatus(0);
        productMapper.save(product); //添加时会自动创建id，useGeneratedKeys="true" keyProperty="id这个的含义是会自动返回id到product对象中

        //保存商品的SKU信息到product_sku表
        List<ProductSku> productSkuList = product.getProductSkuList();
        for (int i = 0; i < productSkuList.size(); i++) {
            ProductSku productSku = productSkuList.get(i);
            productSku.setSkuCode(product.getId() + "_" + i);       // 构建skuCode
            productSku.setProductId(product.getId());               // 设置商品id
            productSku.setSkuName(product.getName() + productSku.getSkuSpec());
            productSku.setSaleNum(0);                               // 设置销量
            productSku.setStatus(0);
            productSkuMapper.save(productSku);
        }

        //保存商品的详细信息到product_details表
        ProductDetails productDetails = new ProductDetails();
        productDetails.setProductId(product.getId());
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.save(productDetails);

    }

    //根据商品id查询商品信息
    @Override
    public Product getById(Long id) {
        //根据id查询商品表 product
        Product product = productMapper.selectById(id);

        //根据id查询商品sku表 product_sku
        List<ProductSku> productSkuList = productSkuMapper.selectByProductId(id);
        product.setProductSkuList(productSkuList);

        //根据id查询商品详细信息表 product_details
        ProductDetails productDetails = productDetailsMapper.selectByProductId(id);
        product.setDetailsImageUrls(productDetails.getImageUrls());

        // 返回数据
        return product;
    }

    //保存修改
    @Override
    public void update(Product product) {
        // 修改商品基本数据 product表
        productMapper.updateById(product);

        // 修改商品的sku数据 product_sku表
        List<ProductSku> productSkuList = product.getProductSkuList();
        productSkuList.forEach(productSku -> {
            productSkuMapper.updateById(productSku);
        });

        // 修改商品的详情数据 product_details表
        ProductDetails productDetails = productDetailsMapper.selectByProductId(product.getId());
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.updateById(productDetails);
    }

    //删除
    @Override
    public void deleteById(Long id) {
        // 根据id删除商品基本数据 product
        productMapper.deleteById(id);
        // 根据商品id删除商品的sku数据 product_sku
        productSkuMapper.deleteByProductId(id);
        // 根据商品的id删除商品的详情数据 product_details
        productDetailsMapper.deleteByProductId(id);
    }

    //商品审核
    @Override
    public void updateAuditStatus(Long id, Integer auditStatus) {
        Product product = new Product();
        product.setId(id);
        if(auditStatus == 1) {
            product.setAuditStatus(1);
            product.setAuditMessage("审批通过");
        } else {
            product.setAuditStatus(-1);
            product.setAuditMessage("审批不通过");
        }
        productMapper.updateById(product);
    }

    //商品上下架
    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        if(status == 1) {
            product.setStatus(1);
        } else {
            product.setStatus(-1);
        }
        productMapper.updateById(product);
    }
}
