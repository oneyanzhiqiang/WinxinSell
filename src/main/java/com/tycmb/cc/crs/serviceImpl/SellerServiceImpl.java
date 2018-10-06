package com.tycmb.cc.crs.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.mapper.SellerInfoMapper;
import com.tycmb.cc.crs.pojo.SellerInfo;
import com.tycmb.cc.crs.service.SellerService;

@Service
public class SellerServiceImpl implements SellerService {
	
	@Autowired
	SellerInfoMapper sellerInfoMapper;

	@Override
	public SellerInfo findSellerInfoByOpenId(String openid) {
		// TODO Auto-generated method stub
		return sellerInfoMapper.selectByOpenId(openid);
	}

}
