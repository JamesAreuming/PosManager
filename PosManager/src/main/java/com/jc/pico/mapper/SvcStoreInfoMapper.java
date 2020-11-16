package com.jc.pico.mapper;

import java.util.HashMap;

import com.jc.pico.bean.SvcStoreInfo;
import com.jc.pico.utils.bean.SingleMap;

public interface SvcStoreInfoMapper {
    SvcStoreInfo selectMerchantAndPrinter(HashMap<String, Object> storeInfo);

	SingleMap selectDeviceLicenseIsMain(HashMap<String, Object> storeInfo);
}