package com.tycmb.cc.crs.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.dto.OrderDTO;
import com.tycmb.cc.crs.mapper.OrderDetailMapper;
import com.tycmb.cc.crs.pojo.OrderDetail;
import com.tycmb.cc.crs.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
	
	@Autowired
	OrderDetailMapper orderDetailMapper;

	@Override
	public int insert(OrderDetail record) {
		// TODO Auto-generated method stub
		return orderDetailMapper.insert(record);
	}

	@Override
	public List<OrderDetail> selectOrderDetailListByOrderId(String orderId) {
		// TODO Auto-generated method stub
		return orderDetailMapper.selectOrderDetailListByOrderId(orderId);
	}

}
