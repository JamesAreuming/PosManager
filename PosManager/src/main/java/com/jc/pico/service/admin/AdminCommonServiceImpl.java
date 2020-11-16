package com.jc.pico.service.admin;

import java.util.Currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.pico.bean.SvcBrand;
import com.jc.pico.mapper.SvcBrandMapper;

@Service
public class AdminCommonServiceImpl implements AdminCommonService {

	@Autowired
	private SvcBrandMapper svcBrandMapper;

	@Override
	public int getBrandFractionDigits(long brandId) {
		SvcBrand brand = svcBrandMapper.selectByPrimaryKey(brandId);
		if (brand == null) {
			return 0;
		}
		Currency currency = Currency.getInstance(brand.getCurrency());
		return currency.getDefaultFractionDigits();
	}

}
