package com.tycmb.cc.crs.converter;


import org.springframework.beans.BeanUtils;

import com.tycmb.cc.crs.dto.OrderDTO;
import com.tycmb.cc.crs.pojo.OrderMaster;

import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Collectors;

/**
 * Created by 廖师兄
 * 2017-06-11 22:02
 */
public class OrderMaster2OrderDTOConverter {

    public static OrderDTO convert(OrderMaster orderMaster) {

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList) {
//        return orderMasterList.stream().map(e ->
//                convert(e)
//        ).collect(Collectors.toList());
    	List<OrderDTO> orderDTOs = new ArrayList();
    	for (int i = 0; i < orderMasterList.size(); i++) {
    		orderDTOs.add(convert(orderMasterList.get(i)));
		}
    	return orderDTOs;
    }
}
