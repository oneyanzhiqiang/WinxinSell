package com.tycmb.cc.crs.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.dto.OrderDTO;

@Service
public interface OrderService {
	
	/** 创建订单. */
    OrderDTO create(OrderDTO orderDTO);
    
    /*查询订单列表*/
    List<OrderDTO> findList(String openid, Integer page, Integer size);
    
    /*查询全部订单*/
    List<OrderDTO> findAllList();

    /*查询单个订单*/
	OrderDTO findOne(String orderId);

	/*删除单个订单*/
	OrderDTO cancel(OrderDTO orderDTO);
	
	/*完成订单*/
	OrderDTO finish(OrderDTO orderDTO);
	
	/*修改订单状态*/
	OrderDTO paid(OrderDTO orderDTO);

	
}
