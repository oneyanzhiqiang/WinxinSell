package com.tycmb.cc.crs.enums;

import lombok.Getter;

/**
 * Created by 廖师兄
 * 2017-06-11 17:12
 */
@Getter
public enum OrderStatusEnum implements CodeEnum {
    NEW(0, "新订单"),
    FINISHED(1, "完结"),
    CANCEL(2, "已取消"),
    ;

    private Integer code;

    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

	@Override
	public Integer getCode() {
		// TODO Auto-generated method stub
		return this.code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
