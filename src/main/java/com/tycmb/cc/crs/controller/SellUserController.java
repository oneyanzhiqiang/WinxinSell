package com.tycmb.cc.crs.controller;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tycmb.cc.crs.config.ProjectUrlConfig;
import com.tycmb.cc.crs.constant.CookieConstant;
import com.tycmb.cc.crs.constant.RedisConstant;
import com.tycmb.cc.crs.enums.ResultEnum;
import com.tycmb.cc.crs.pojo.SellerInfo;
import com.tycmb.cc.crs.serviceImpl.SellerServiceImpl;
import com.tycmb.cc.crs.utils.CookieUtil;

@Controller
@RequestMapping("/seller")
public class SellUserController {
	
	@Autowired
	SellerServiceImpl sellerServicel;
	
	@Autowired
    private StringRedisTemplate redisTemplate;
	
	@Autowired
    private ProjectUrlConfig projectUrlConfig;

	//login
	@GetMapping("/login")
	public ModelAndView login(@RequestParam("openid") String openid, 
			                  HttpServletResponse response,
			                  Map<String, Object> map) {
		
		//数据库里查询openid
		SellerInfo sellerInfo = sellerServicel.findSellerInfoByOpenId(openid);
		if (sellerInfo == null) {
			map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error");
		}
		
		//设置token到redis中
		String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;
//      redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), openid, expire, TimeUnit.SECONDS);
		
        //设置token到cookie中
        CookieUtil.set(response, CookieConstant.TOKEN, token, expire);

        return new ModelAndView("redirect:" + projectUrlConfig.getSell() + "/sell/seller/order/list");	
	}
	
	//logout
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Map<String, Object> map) {
        //1. 从cookie里查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null) {
            //2. 清除redis
//            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));

            //3. 清除cookie
            CookieUtil.set(response, CookieConstant.TOKEN, null, 0);
        }

        map.put("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url", "/sell/seller/order/list");
        return new ModelAndView("common/success", map);
    }
}
