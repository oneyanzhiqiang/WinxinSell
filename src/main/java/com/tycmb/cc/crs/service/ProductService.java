package com.tycmb.cc.crs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.pojo.ProductInfo;

@Service
public interface ProductService {
	
	List<ProductInfo> findUpAll();
	
	List<ProductInfo> findAll();
	
	ProductInfo onSale(String productId);
	
	ProductInfo offSale(String productId);
	
	ProductInfo findOne(String productId);
	
	int save(ProductInfo productInfo);
	
	int update(ProductInfo productInfo);

}
