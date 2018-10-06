package com.tycmb.cc.crs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.dto.OrderDTO;
import com.tycmb.cc.crs.pojo.OrderMaster;

@Service
public interface OrderMasterService {

	public int insert(OrderMaster record);
	
	public List<OrderMaster> findByBuyerOpenid(String openid, Integer page, Integer size) ;

	public OrderMaster findOne(String orderId);

	OrderMaster deleteOrderMasterByOrderId(String orderId);
	
	int updateOrderMaster(OrderMaster orderMaster);
	
	List<OrderMaster> findAllOrderMaster();
	
	int update(OrderMaster orderMaster);
}
