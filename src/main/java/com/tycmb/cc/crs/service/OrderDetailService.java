package com.tycmb.cc.crs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.pojo.OrderDetail;

@Service
public interface OrderDetailService {
	
	public int insert(OrderDetail record);
	
	public List<OrderDetail> selectOrderDetailListByOrderId(String orderId);
}
