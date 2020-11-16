package com.jc.pico.utils.customMapper.store;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jc.pico.bean.SvcDeviceLicense;
import com.jc.pico.utils.bean.SingleMap;

public interface StoreCommonMapper {

	List<SingleMap> selectBrandListByFrndId(SingleMap param);

	List<SingleMap> selectStoreListByBrandId(SingleMap param);

	List<SingleMap> getStoreServiceListByStoreId(SingleMap param);

	String selectBaseCodeByAlias(@Param("mainCd") String mainCd, @Param("alias") String alias);

	String selectCurrencyByBrandId(SingleMap param);

	String selectCurrencyByStoreId(SingleMap param);

	SingleMap selectFranchiseInfoByFranId(SingleMap param);

	SingleMap selectBrandInfoByBrandId(SingleMap param);

	SingleMap selectStoreInfoByStoreId(SingleMap param);

	SingleMap selectUserMappingByUserName(SingleMap param);

	Long selectBrandIdByStoreId(long storeId);

	SvcDeviceLicense selectPosNoByLicenseId(SingleMap map);

}
