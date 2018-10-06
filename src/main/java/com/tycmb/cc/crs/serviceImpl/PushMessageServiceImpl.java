package com.tycmb.cc.crs.serviceImpl;



import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceApacheHttpClientImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.config.WechatAccountConfig;
import com.tycmb.cc.crs.dto.OrderDTO;
import com.tycmb.cc.crs.service.PushMessageService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 廖师兄
 * 2017-07-30 22:09
 */
@Service
public class PushMessageServiceImpl implements PushMessageService {

    @Autowired
    private WechatAccountConfig accountConfig;
    
    private Log logger = LogFactory.getLog(PushMessageServiceImpl.class);

    @Override
    public void orderStatus(OrderDTO orderDTO) {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setTemplateId(accountConfig.getTemplateId().get("orderStatus"));
        templateMessage.setToUser(orderDTO.getBuyerOpenid());

        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("first", "亲，请记得收货。"),
                new WxMpTemplateData("keyword1", "微信点餐"),
                new WxMpTemplateData("keyword2", "18868812345"),
                new WxMpTemplateData("keyword3", orderDTO.getOrderId()),
                new WxMpTemplateData("keyword4", orderDTO.getOrderStatusEnum().getMessage()),
                new WxMpTemplateData("keyword5", "￥" + orderDTO.getOrderAmount()),
                new WxMpTemplateData("remark", "欢迎再次光临！")
        );
        templateMessage.setData(data);
        try {
        	WxMpService wxMpService = new WxMpServiceApacheHttpClientImpl();
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        }catch (WxErrorException e) {
        	logger.error("【微信模版消息】发送失败, {}", e);
        }
    }
}
