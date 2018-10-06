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

import com.tycmb.cc.crs.exception.SellException;
import com.tycmb.cc.crs.from.CategoryForm;
import com.tycmb.cc.crs.pojo.ProductCategory;
import com.tycmb.cc.crs.serviceImpl.CategoryServiceImpl;

@RestController
@RequestMapping("/seller/category")
public class SellerCategoryController {
	
	@Autowired
	CategoryServiceImpl categoryService;

	/**
	 * 卖家类目列表展示
	 * @param map
	 * @return
	 */
	@GetMapping("list")
	public ModelAndView list(Map<String, Object> map){
		
		List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList", categoryList);
        return new ModelAndView("category/list", map);
	}
	
	/**
	 * 类目展示
	 * @param categoryId
	 * @param map
	 * @return
	 */
	@GetMapping("index")
	public ModelAndView index(@RequestParam(value = "categoryId", required = false) Integer categoryId,
										Map<String, Object> map){
		if (categoryId != null) {
			ProductCategory productCategory = categoryService.selectByPrimaryKey(categoryId);
			map.put("category", productCategory);
		}
		
		return new ModelAndView("category/index", map);
	}
	
    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm form,
                             BindingResult bindingResult,
                             Map<String, Object> map) {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/sell/seller/category/index");
            return new ModelAndView("common/error", map);
        }

        ProductCategory productCategory = new ProductCategory();
        try {
            if (form.getCategoryId() != null) {
                productCategory = categoryService.findOne(form.getCategoryId());
                BeanUtils.copyProperties(form, productCategory);
                categoryService.update(productCategory);
                
            }else {				
            	BeanUtils.copyProperties(form, productCategory);
            	categoryService.save(productCategory);
			}
            
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/category/index");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/sell/seller/category/list");
        return new ModelAndView("common/success", map);
    }
	
	
}
