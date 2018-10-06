package com.tycmb.cc.crs.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.tycmb.cc.crs.exception.SellException;
import com.tycmb.cc.crs.from.ProductForm;
import com.tycmb.cc.crs.pojo.ProductCategory;
import com.tycmb.cc.crs.pojo.ProductInfo;
import com.tycmb.cc.crs.serviceImpl.CategoryServiceImpl;
import com.tycmb.cc.crs.serviceImpl.ProductServiceImpl;
import com.tycmb.cc.crs.utils.KeyUtil;

@RestController
@RequestMapping("/seller/product")
public class SellerProductController {

	@Autowired
	ProductServiceImpl productService;
	
	@Autowired
	CategoryServiceImpl categoryService;
	
	
	//商品列表
	@GetMapping("/list")
	public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
            				 @RequestParam(value = "size", defaultValue = "10") Integer size,
            				 Map<String, Object> map){
		
		List<ProductInfo> productInfoList = productService.findAll();
		
		map.put("productInfoPage", productInfoList);
        map.put("currentPage", page);
        map.put("size", size);
		return new ModelAndView("product/list", map);
	}
	//商品上架
	@RequestMapping("/on_sale")
	public ModelAndView onSale(@RequestParam("productId") String productId, Map<String, Object> map){
		
		try {
			productService.onSale(productId);
		} catch (Exception e) {
			map.put("msg", e.getMessage());
			map.put("url", "/sell/seller/product/list");
			return new ModelAndView("common/error", map);
		}
		map.put("url", "/sell/seller/product/list");
		return new ModelAndView("common/success", map);
	}
	//商品下架
    @RequestMapping("/off_sale")
    public ModelAndView offSale(@RequestParam("productId") String productId,
                               Map<String, Object> map) {
        try {
            productService.offSale(productId);
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/product/list");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }
	//保存更新
    @PostMapping("/save")
    public ModelAndView save(@Valid ProductForm form,
                             BindingResult bindingResult,
                             Map<String, Object> map) {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/sell/seller/product/index");
            return new ModelAndView("common/error", map);
        }

        ProductInfo productInfo = new ProductInfo();
        try {
            //如果productId为空, 说明是新增
            if (!StringUtils.isEmpty(form.getProductId())) {
                productInfo = productService.findOne(form.getProductId());
                BeanUtils.copyProperties(form, productInfo);
                productService.update(productInfo);
            } else {
                form.setProductId(KeyUtil.genUniqueKey());
                BeanUtils.copyProperties(form, productInfo);
                productService.save(productInfo);
            }
            
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/product/index");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }
    
    
    
    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "productId", required = false) String productId,
                      Map<String, Object> map) {
        if (!StringUtils.isEmpty(productId)) {
            ProductInfo productInfo = productService.findOne(productId);
            map.put("productInfo", productInfo);
        }

        //查询所有的类目
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList", categoryList);

        return new ModelAndView("product/index", map);
    }
}
