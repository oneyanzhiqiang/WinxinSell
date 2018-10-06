package com.tycmb.cc.crs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tycmb.cc.crs.pojo.ProductInfo;
import com.tycmb.cc.crs.pojo.ProductInfoExample;

public interface ProductInfoMapper {
    int countByExample(ProductInfoExample example);

    int deleteByExample(ProductInfoExample example);

    int deleteByPrimaryKey(String productId);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    List<ProductInfo> selectByExample(ProductInfoExample example);

    ProductInfo selectByPrimaryKey(String productId);

    int updateByExampleSelective(@Param("record") ProductInfo record, @Param("example") ProductInfoExample example);

    int updateByExample(@Param("record") ProductInfo record, @Param("example") ProductInfoExample example);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);
    
    List<ProductInfo> selectAllProductInfoByStatus(Integer productStatus);

	List<ProductInfo> selectAllProductInfo();

}