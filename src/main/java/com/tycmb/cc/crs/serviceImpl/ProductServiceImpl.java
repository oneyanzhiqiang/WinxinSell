package com.tycmb.cc.crs.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.enums.ProductStatusEnum;
import com.tycmb.cc.crs.enums.ResultEnum;
import com.tycmb.cc.crs.exception.SellException;
import com.tycmb.cc.crs.mapper.ProductInfoMapper;
import com.tycmb.cc.crs.pojo.ProductInfo;
import com.tycmb.cc.crs.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductInfoMapper productInfoMapper;

	@Override
	public List<ProductInfo> findUpAll() {
		// TODO Auto-generated method stub
		return productInfoMapper.selectAllProductInfoByStatus(ProductStatusEnum.UP.getCode());
	}

	@Override
	public List<ProductInfo> findAll() {
		// TODO Auto-generated method stub
		return productInfoMapper.selectAllProductInfo();
	}

	@Override
	public ProductInfo onSale(String productId) {
		
		ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
		if (productInfo == null) {
			throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
		}
		
		if (productInfo.getProductStatus() == (ProductStatusEnum.UP.getCode().toString().getBytes()[0])) {
			throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
		}
		
		//更新状态
		productInfo.setProductStatus(ProductStatusEnum.UP.getCode().toString().getBytes()[0]);
		productInfoMapper.updateByPrimaryKey(productInfo);
		
		return productInfo;
	}

	@Override
	public ProductInfo offSale(String productId) {
		ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
		if (productInfo == null) {
			throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
		}
		
		if (productInfo.getProductStatus() == (ProductStatusEnum.DOWN.getCode().toString().getBytes()[0])) {
			throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
		}
		
		//更新状态
		productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode().toString().getBytes()[0]);
		productInfoMapper.updateByPrimaryKey(productInfo);
		return productInfo;
	}

	@Override
	public ProductInfo findOne(String productId) {
		// TODO Auto-generated method stub
		return productInfoMapper.selectByPrimaryKey(productId);
	}

	@Override
	public int save(ProductInfo productInfo) {
		// TODO Auto-generated method stub
		return productInfoMapper.insert(productInfo);
	}

	@Override
	public int update(ProductInfo productInfo) {
		// TODO Auto-generated method stub
		return productInfoMapper.updateByPrimaryKey(productInfo);
	}
	


}
