package com.tycmb.cc.crs.serviceImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.dto.OrderDTO;
import com.tycmb.cc.crs.enums.ResultEnum;
import com.tycmb.cc.crs.exception.SellException;
import com.tycmb.cc.crs.service.BuyerService;

@Service
@Mapper
public class BuyerServiceImpl implements BuyerService {
	
	private Log logger = LogFactory.getLog(BuyerServiceImpl.class); 
	
	@Autowired
    private OrderServiceImpl orderService;

	@Override
	public OrderDTO findOrderOne(String openid, String orderId) {
		
		return checkOrderOwner(openid, orderId);
	}

	@Override
	public OrderDTO cancelOrder(String openid, String orderId) {
		OrderDTO orderDTO = checkOrderOwner(openid, orderId);
		if (orderDTO == null) {
			logger.error("【取消订单】查不到改订单, orderId={}"+orderId);
			throw new SellException(ResultEnum.ORDER_NOT_EXIST);
		}
		return orderService.cancel(orderDTO);
	}
	
	private OrderDTO checkOrderOwner(String openid, String orderId) {
		
		OrderDTO orderDTO = orderService.findOne(orderId);
		if (orderDTO == null) {
			return null;
		}
		
//		判断是否为自己的订单
		if (!orderDTO.getBuyerOpenid().equalsIgnoreCase(openid)) {
			logger.error("【查询订单】订单的openid不一致. openid={}, orderDTO={}"+ openid + orderId);
			throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
		}
		return orderDTO;
	}
	

}
