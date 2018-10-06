package com.tycmb.cc.crs.service;

import com.tycmb.cc.crs.pojo.SellerInfo;

public interface SellerService {

	SellerInfo findSellerInfoByOpenId(String openid); 
}
