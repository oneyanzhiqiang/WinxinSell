package com.tycmb.cc.crs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.dto.CartDTO;
import com.tycmb.cc.crs.pojo.ProductInfo;

@Service
public interface ProductInfoService {
	
	public int insert(ProductInfo record);
	
	ProductInfo selectByPrimaryKey(String productId);
	
	void increaseStock(List<CartDTO> cartDTOList);
	
	void decreaseStock(List<CartDTO> cartDTOList);

}
