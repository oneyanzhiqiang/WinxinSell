package com.tycmb.cc.crs.serviceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tycmb.cc.crs.controller.BuyerOrderController;
import com.tycmb.cc.crs.converter.OrderMaster2OrderDTOConverter;
import com.tycmb.cc.crs.dto.CartDTO;
import com.tycmb.cc.crs.dto.OrderDTO;
import com.tycmb.cc.crs.enums.OrderStatusEnum;
import com.tycmb.cc.crs.enums.PayStatusEnum;
import com.tycmb.cc.crs.enums.ResultEnum;
import com.tycmb.cc.crs.exception.SellException;
import com.tycmb.cc.crs.mapper.OrderMasterMapper;
import com.tycmb.cc.crs.pojo.OrderDetail;
import com.tycmb.cc.crs.pojo.OrderMaster;
import com.tycmb.cc.crs.pojo.ProductInfo;
import com.tycmb.cc.crs.service.OrderService;
import com.tycmb.cc.crs.service.WebSocket;
import com.tycmb.cc.crs.utils.KeyUtil;

@Component
@Mapper
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	ProductInfoServiceImpl productInfoService;
	
	@Autowired
	OrderDetailServiceImpl orderDetailService;
	
	@Autowired
	OrderMasterServiceImpl orderMasterService;
	
	@Autowired
    private WebSocket webSocket;
	
	private Log logger = LogFactory.getLog(OrderServiceImpl.class);

	@Override
	public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

//        List<CartDTO> cartDTOList = new ArrayList<>();

        //1. 查询商品（数量, 价格）
        for (OrderDetail orderDetail: orderDTO.getOrderDetailList()) {
            ProductInfo productInfo =  productInfoService.selectByPrimaryKey(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            //2. 计算订单总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            //订单详情入库
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailService.insert(orderDetail);

//            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
//            cartDTOList.add(cartDTO);
        }


        //3. 写入订单数据库（orderMaster和orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterService.insert(orderMaster);

        //4. 扣库存
        List<CartDTO> cartDTOList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
        	cartDTOList.add(new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity()));
        }     
        productInfoService.decreaseStock(cartDTOList);

        //发送websocket消息
        webSocket.sendMessage(orderDTO.getOrderId());

        return orderDTO;
	}

	@Override
	public List<OrderDTO> findList(String openid, Integer page, Integer size) {
		List<OrderMaster> orderMasterList = orderMasterService.findByBuyerOpenid(openid, page, size);
		
		List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterList);
		
		return orderDTOList;
	}
	
	@Override
	public OrderDTO findOne(String orderId){
		
		OrderMaster orderMaster = orderMasterService.findOne(orderId);
		if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
		List<OrderDetail> orderDetailList = orderDetailService.selectOrderDetailListByOrderId(orderId);
		
		if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
		
		OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
		
		return orderDTO;
	}

	@Override
	public OrderDTO cancel(OrderDTO orderDTO) {

		OrderMaster orderMaster = new OrderMaster();
		
		/*判断订单状态*/
		if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            logger.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}"+ orderDTO.getOrderId() + orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
		
		//修改订单状态
		orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
		BeanUtils.copyProperties(orderDTO, orderMaster);
		int updateResult = orderMasterService.updateOrderMaster(orderMaster);
		if (updateResult == 0) {
			logger.error("【取消订单】更新失败, orderMaster={}" + orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
		}
		
		//返回库存
		if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
			logger.error("【取消订单】订单中无商品详情, orderDTO={}" + orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
		}
		
		List<CartDTO> cartDTOList = new ArrayList<>();
		for (int i = 0; i < orderDTO.getOrderDetailList().size(); i++) {
			cartDTOList.add(new CartDTO(orderDTO.getOrderDetailList().get(i).getProductId(),
					                    orderDTO.getOrderDetailList().get(i).getProductQuantity()));
		}
		
		productInfoService.increaseStock(cartDTOList);
		
		//如果已支付，需要退款
//		if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
//            payService.refund(orderDTO);
//        }
		
		return orderDTO;
	}

	@Override
	public List<OrderDTO> findAllList() {
		// TODO Auto-generated method stub
		List<OrderMaster> orderMasterList = orderMasterService.findAllOrderMaster();
		
		List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterList);
		
		return orderDTOList;
	}

	@Override
	@Transactional
	public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            logger.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}"+ orderDTO.getOrderId()+"---"+orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        int updateResult = orderMasterService.updateOrderMaster(orderMaster);
        if (updateResult == 0) {
            logger.error("【完结订单】更新失败, orderMaster={}"+orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //推送微信模版消息
        //pushMessageService.orderStatus(orderDTO);

        return orderDTO;
	}

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            logger.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}" + orderDTO.getOrderId() + orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
        	logger.error("【订单支付完成】订单支付状态不正确, orderDTO={}" + orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        int updateResult = orderMasterService.update(orderMaster);
        if (updateResult == 0) {
        	logger.error("【订单支付完成】更新失败, orderMaster={}" + orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }



}
