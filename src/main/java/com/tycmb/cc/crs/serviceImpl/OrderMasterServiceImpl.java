package com.tycmb.cc.crs.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.dto.OrderDTO;
import com.tycmb.cc.crs.mapper.OrderMasterMapper;
import com.tycmb.cc.crs.pojo.OrderMaster;
import com.tycmb.cc.crs.service.OrderMasterService;
import com.tycmb.cc.crs.utils.PageBean;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {

	@Autowired
	OrderMasterMapper orderMasterMapper;
	@Override
	public int insert(OrderMaster record) {
		// TODO Auto-generated method stub
		return orderMasterMapper.insert(record);
	}
	
	@Override
	public List<OrderMaster> findByBuyerOpenid(String openid, Integer page, Integer size) {
		
		//根据微信号openid获取全部订单列表
		List<OrderMaster> orderMasterList = orderMasterMapper.selectOrderMasterListByOpenId(openid);
		
		int countNums = orderMasterList.size();           //总记录数
        PageBean<OrderMaster> pageData = new PageBean<>(page, size, countNums);
        pageData.setItems(orderMasterList);
        return pageData.getItems();
	}

	@Override
	public OrderMaster findOne(String orderId) {
		// TODO Auto-generated method stub
		return orderMasterMapper.selectOrderMasterByOrderId(orderId);
	}

	@Override
	public OrderMaster deleteOrderMasterByOrderId(String orderId) {
		// TODO Auto-generated method stub
		return orderMasterMapper.deleteOrderMasterByOrderId(orderId);
	}

	@Override
	public int updateOrderMaster(OrderMaster orderMaster) {
		// TODO Auto-generated method stub
		return orderMasterMapper.updateByPrimaryKey(orderMaster);
	}
	
	@Override
	public List<OrderMaster> findAllOrderMaster() {
		// TODO Auto-generated method stub
		return orderMasterMapper.seleteAllOrderMaster();
	}

	@Override
	public int update(OrderMaster orderMaster) {
		// TODO Auto-generated method stub
		return orderMasterMapper.updateByPrimaryKey(orderMaster);
	}
	

}
