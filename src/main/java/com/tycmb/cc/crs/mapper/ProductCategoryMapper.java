package com.tycmb.cc.crs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tycmb.cc.crs.pojo.ProductCategory;
import com.tycmb.cc.crs.pojo.ProductCategoryExample;

public interface ProductCategoryMapper {
    int countByExample(ProductCategoryExample example);

    int deleteByExample(ProductCategoryExample example);

    int deleteByPrimaryKey(Integer categoryId);

    int insert(ProductCategory record);

    int insertSelective(ProductCategory record);

    List<ProductCategory> selectByExample(ProductCategoryExample example);

    ProductCategory selectByPrimaryKey(Integer categoryId);
    
    ProductCategory selectByProductCategoryType(Integer productCategoryType);

    int updateByExampleSelective(@Param("record") ProductCategory record, @Param("example") ProductCategoryExample example);

    int updateByExample(@Param("record") ProductCategory record, @Param("example") ProductCategoryExample example);

    int updateByPrimaryKeySelective(ProductCategory record);

    int updateByPrimaryKey(ProductCategory record);

	List<ProductCategory> seleceAll();
}