package com.tycmb.cc.crs.aspect;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tycmb.cc.crs.constant.CookieConstant;
import com.tycmb.cc.crs.constant.RedisConstant;
import com.tycmb.cc.crs.exception.SellerAuthorizeException;
import com.tycmb.cc.crs.utils.CookieUtil;

/**
 * Created by 廖师兄
 * 2017-07-30 17:31
 */
@Aspect
@Component
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private Log logger = LogFactory.getLog(SellerAuthorizeAspect.class);

    @Pointcut("execution(public * com.tycmb.cc.crs.controller.Seller*.*(..))" +
    "&& !execution(public * com.tycmb.cc.crs.controller.SellUserController.*(..))")
    public void verify() {}

    @Before("verify()")
    public void doVerify() {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//
//        //查询cookie
//        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
//        if (cookie == null) {
//        	logger.warn("【登录校验】Cookie中查不到token");
//            throw new SellerAuthorizeException();
//        }
//
//        //去redis里查询
//        String tokenValue = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
//        if (StringUtils.isEmpty(tokenValue)) {
//        	logger.warn("【登录校验】Redis中查不到token");
//            throw new SellerAuthorizeException();
//        }
    }
}
