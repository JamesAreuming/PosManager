package com.jc.pico.mapper;

import java.util.List;

import com.jc.pico.bean.SvcStoreMulti;
import com.jc.pico.utils.bean.SingleMap;

public interface SvcStoreMultiMapper{
	SingleMap selectBySpecialtyInfo(SingleMap param);
}
