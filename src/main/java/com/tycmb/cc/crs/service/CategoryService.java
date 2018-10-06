package com.tycmb.cc.crs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.pojo.ProductCategory;

@Service
public interface CategoryService {
	
	ProductCategory findByCategoryTypeIn(Integer productCategoryType);
	
	ProductCategory selectByPrimaryKey(Integer categoryId);
	
	List<ProductCategory> findAll();
	
	ProductCategory findOne(Integer categoryId);
	
	int save(ProductCategory productCategory);
	
	int update(ProductCategory productCategory);

}
