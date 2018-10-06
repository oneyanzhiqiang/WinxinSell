package com.tycmb.cc.crs.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by 廖师兄
 * 2017-07-03 00:50
 */
@RestController
@RequestMapping("/weixin")
public class WeixinController {
	
	private Log logger = LogFactory.getLog(WeixinController.class);
    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code) {
    	logger.info("进入auth方法。。。");
    	logger.info("code={}" + code);

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxd898fcb01713c658&secret=29d8a650db31472aa87800e3b0d739f2&code=" + code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        logger.info("response={}" + response);
    }
}
