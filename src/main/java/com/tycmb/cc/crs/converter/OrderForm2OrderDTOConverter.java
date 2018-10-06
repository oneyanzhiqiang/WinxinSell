package com.tycmb.cc.crs.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tycmb.cc.crs.dto.OrderDTO;
import com.tycmb.cc.crs.enums.ResultEnum;
import com.tycmb.cc.crs.exception.SellException;
import com.tycmb.cc.crs.from.OrderForm;
import com.tycmb.cc.crs.pojo.OrderDetail;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class OrderForm2OrderDTOConverter {
	
	private static Log logger=LogFactory.getLog(OrderForm2OrderDTOConverter.class);

    public static OrderDTO convert(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());
        } catch (Exception e) {
        	logger.info("【对象转换】错误, string={}"+ orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}
