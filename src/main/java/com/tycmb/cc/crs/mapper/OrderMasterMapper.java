package com.tycmb.cc.crs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tycmb.cc.crs.pojo.OrderMaster;
import com.tycmb.cc.crs.pojo.OrderMasterExample;

public interface OrderMasterMapper {
    int countByExample(OrderMasterExample example);

    int deleteByExample(OrderMasterExample example);

    int deleteByPrimaryKey(String orderId);

    int insert(OrderMaster record);

    int insertSelective(OrderMaster record);

    List<OrderMaster> selectByExample(OrderMasterExample example);

    OrderMaster selectByPrimaryKey(String orderId);

    int updateByExampleSelective(@Param("record") OrderMaster record, @Param("example") OrderMasterExample example);

    int updateByExample(@Param("record") OrderMaster record, @Param("example") OrderMasterExample example);

    int updateByPrimaryKeySelective(OrderMaster record);

    
    int updateByPrimaryKey(OrderMaster record);
    
    List<OrderMaster> selectOrderMasterListByOpenId(@Param("buyerOpenid") String openid);

    OrderMaster selectOrderMasterByOrderId(@Param("orderId") String orderId);
    
    OrderMaster deleteOrderMasterByOrderId(@Param("orderId") String orderId);

	List<OrderMaster> seleteAllOrderMaster();

    
}