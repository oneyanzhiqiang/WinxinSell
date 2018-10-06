package com.tycmb.cc.crs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.tycmb.cc.crs.VO.ProductInfoVO;
import com.tycmb.cc.crs.VO.ProductVO;
import com.tycmb.cc.crs.VO.ResultVO;
import com.tycmb.cc.crs.pojo.ProductCategory;
import com.tycmb.cc.crs.pojo.ProductInfo;
import com.tycmb.cc.crs.serviceImpl.CategoryServiceImpl;
import com.tycmb.cc.crs.serviceImpl.ProductServiceImpl;
import com.tycmb.cc.crs.utils.ResultVOUtil;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
	
	@Autowired
    private ProductServiceImpl productService;
	
	@Autowired
    private CategoryServiceImpl categoryService;
	
	private Log logger = LogFactory.getLog(BuyerOrderController.class);

	@GetMapping("/list")
	public ResultVO list(){
		
		//查询所有上架的列表
		List<ProductInfo> productInfoList =  productService.findUpAll();
		
		//查询类目
	      List<Integer> categoryTypeList = new ArrayList<>();
	      List<ProductCategory> productCategoryList = new ArrayList<>();
		  for (ProductInfo productInfo : productInfoList) {
			  categoryTypeList.add(productInfo.getCategoryType());
			  productCategoryList.add(categoryService.findByCategoryTypeIn(productInfo.getCategoryType()));
	      }
					
		//数据拼装
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory: productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo: productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        
        return ResultVOUtil.success(productVOList);		
	}
}
