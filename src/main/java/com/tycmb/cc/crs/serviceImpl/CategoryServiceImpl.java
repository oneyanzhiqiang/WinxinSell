package com.tycmb.cc.crs.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tycmb.cc.crs.mapper.ProductCategoryMapper;
import com.tycmb.cc.crs.pojo.ProductCategory;
import com.tycmb.cc.crs.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	ProductCategoryMapper productCategoryMapper;
	
	@Override
	public ProductCategory findByCategoryTypeIn(Integer productCategoryType) {
		// TODO Auto-generated method stub
		return productCategoryMapper.selectByProductCategoryType(productCategoryType);
	}

	@Override
	public List<ProductCategory> findAll() {
		// TODO Auto-generated method stub
		return productCategoryMapper.seleceAll();
	}

	@Override
	public ProductCategory findOne(Integer categoryId) {
		// TODO Auto-generated method stub
		return productCategoryMapper.selectByPrimaryKey(categoryId);
	}

	@Override
	public int save(ProductCategory productCategory) {
		// TODO Auto-generated method stub
		return productCategoryMapper.insert(productCategory);
	}

	@Override
	public ProductCategory selectByPrimaryKey(Integer categoryId) {
		// TODO Auto-generated method stub
		return productCategoryMapper.selectByPrimaryKey(categoryId);
	}

	@Override
	public int update(ProductCategory productCategory) {
		// TODO Auto-generated method stub
		return productCategoryMapper.updateByPrimaryKey(productCategory);
	}

}
