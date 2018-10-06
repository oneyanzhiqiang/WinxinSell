package com.tycmb.cc.crs.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tycmb.cc.crs.dto.CartDTO;
import com.tycmb.cc.crs.enums.ResultEnum;
import com.tycmb.cc.crs.exception.SellException;
import com.tycmb.cc.crs.mapper.ProductInfoMapper;
import com.tycmb.cc.crs.pojo.ProductInfo;
import com.tycmb.cc.crs.service.ProductInfoService;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
	
	@Autowired
	ProductInfoMapper productInfoMapper;

	@Override
	public int insert(ProductInfo record) {
		// TODO Auto-generated method stub
		return productInfoMapper.insert(record);
	}

	@Override
	public ProductInfo selectByPrimaryKey(String productId) {
		// TODO Auto-generated method stub
		return productInfoMapper.selectByPrimaryKey(productId);
	}

	@Override
	@Transactional
	public void increaseStock(List<CartDTO> cartDTOList) {
		
		for (CartDTO cartDTO: cartDTOList) {
			ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(cartDTO.getProductId());
			if (productInfo == null) {
				throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
			}
			Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(result);
            
            productInfoMapper.updateByPrimaryKey(productInfo);
		}
	}
	
    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO: cartDTOList) {
            ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(cartDTO.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if (result < 0) {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productInfo.setProductStock(result);

            productInfoMapper.updateByPrimaryKey(productInfo);
        }
    }
			

}
