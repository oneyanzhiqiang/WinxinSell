package com.tycmb.cc.crs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.tycmb.cc.crs.VO.ResultVO;
import com.tycmb.cc.crs.converter.OrderForm2OrderDTOConverter;
import com.tycmb.cc.crs.dto.OrderDTO;
import com.tycmb.cc.crs.enums.ResultEnum;
import com.tycmb.cc.crs.exception.SellException;
import com.tycmb.cc.crs.from.OrderForm;
import com.tycmb.cc.crs.service.BuyerService;
import com.tycmb.cc.crs.service.OrderService;
import com.tycmb.cc.crs.serviceImpl.BuyerServiceImpl;
import com.tycmb.cc.crs.serviceImpl.OrderServiceImpl;
import com.tycmb.cc.crs.utils.ResultVOUtil;



@RestController
@RequestMapping("/buyer/order")
public class BuyerOrderController {

	@Autowired
	OrderServiceImpl orderService;
	
	@Autowired
	BuyerServiceImpl buyerService;

	private Log logger = LogFactory.getLog(BuyerOrderController.class);

	// 创建订单
	@RequestMapping("/creat")
	public ResultVO<Map<String, String>> creat(@Valid OrderForm orderForm, BindingResult bindingResult){
		
		if (bindingResult.hasErrors()) {
			logger.info("【创建订单】参数不正确, orderForm=" + orderForm.toString());
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
		}
		
		OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
		OrderDTO createResult = orderService.create(orderDTO);
		
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
        	logger.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
		
        Map<String, String> map = new HashMap<>();
        map.put("orderId", createResult.getOrderId());

        return ResultVOUtil.success(map);
	}

	// 订单列表
	@RequestMapping("/list")
	public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid, 
										@RequestParam(value = "page", defaultValue = "0") Integer page,
										@RequestParam(value = "size", defaultValue = "10") Integer size) {

		if (StringUtils.isEmpty(openid)) {
			logger.error("【查询订单列表】openid为空");
			throw new SellException(ResultEnum.PARAM_ERROR);
		}
		
		List<OrderDTO> orderDTOList = orderService.findList(openid, page, size);
		
		return ResultVOUtil.success(orderDTOList);
	}
	
	//订单详情
	@GetMapping("/detail")
	public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
									 @RequestParam("orderId") String orderId){
		
		OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(orderDTO);
	}
	
	//取消订单
	@PostMapping("/cancel")
	public ResultVO cancel(@RequestParam("openid") String openid,
						   @RequestParam("orderId") String orderId){
			
		buyerService.cancelOrder(openid, orderId);
		return ResultVOUtil.success();
	}

}
