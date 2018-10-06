package com.tycmb.cc.crs.service;

import com.tycmb.cc.crs.dto.OrderDTO;

public interface BuyerService {
	
    //查询一个订单
    OrderDTO findOrderOne(String openid, String orderId);

    //取消订单
    OrderDTO cancelOrder(String openid, String orderId);

}
