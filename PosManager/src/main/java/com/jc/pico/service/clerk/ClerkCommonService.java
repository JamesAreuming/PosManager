package com.jc.pico.service.clerk;

import java.util.List;

import javax.servlet.ServletContext;

import com.jc.pico.bean.SvcClosing;
import com.jc.pico.bean.SvcStaff;
import com.jc.pico.bean.SvcStore;
import com.jc.pico.exception.DataNotFoundException;
import com.jc.pico.exception.InvalidParamException;
import com.jc.pico.exception.NoPermissionException;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.utils.bean.SingleMap;

/**
 * 스토어 기본 정보, 테이블 섹션, 테이블, 메뉴 카테고리, 메뉴 항목
 * 
 * @author hyo
 *
 */
public interface ClerkCommonService {

	List<SingleMap> getStoreTableSections(SingleMap param) throws RequestResolveException;

	List<SingleMap> getCategoriesDetail(SingleMap param);

	SingleMap getUserDetail(SingleMap param);

	SingleMap registerDeviceLicense(SingleMap param) throws RequestResolveException, InvalidParamException, NoPermissionException;
	
	/**
	 *  키오스크 디바이스 등록
	 *  추가 : 
	 *  
	 * @param param
	 * @return
	 * @throws RequestResolveException
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 */
	SingleMap registerDeviceLicenseKiosk(SingleMap param) throws RequestResolveException, InvalidParamException, NoPermissionException;
	
	/**
	 * 키오스크 상점 정보
	 * 추가 : 
	 * 
	 * @param param
	 * @return
	 * @throws RequestResolveException
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 */
	SingleMap getStoreInfoKiosk(SingleMap param) throws RequestResolveException, InvalidParamException, NoPermissionException;
	
	
	/**
	 *  키오스크 디바이스와 상점 정보 리턴
	 *  추가 : 
	 *  
	 * @param param
	 * @return
	 * @throws RequestResolveException
	 * @throws InvalidParamException
	 * @throws NoPermissionException
	 */
	SingleMap getDeviceInfoKiosk(SingleMap param) throws RequestResolveException, InvalidParamException, NoPermissionException;
	
	
	
	SingleMap updateLicensePosNo(SingleMap param) throws RequestResolveException, InvalidParamException, NoPermissionException;

	List<SvcStaff> getStaffList(SingleMap param) throws InvalidParamException;

	boolean checkStaffValidation(SingleMap param) throws NoPermissionException;

	boolean checkUserValidation(SingleMap param) throws NoPermissionException;

	SingleMap getAppInfo(SingleMap param) throws RequestResolveException;

	void throwIfPosNotReady(long storeId) throws RequestResolveException;

	SingleMap getStaffDetail(SingleMap param) throws DataNotFoundException;

	List<String> getKichenMessage(SingleMap param) throws RequestResolveException;

	SingleMap getStoreStatus(SingleMap param) throws RequestResolveException;

	SvcClosing getLatestClosingByStoreId(long storeId);
	
	
	/**
	 * 광고정보 가져오기
	 * @param param
	 * @return
	 * @throws RequestResolveException
	 */
	SingleMap getAdvertiseInfo(ServletContext context, SingleMap param) throws RequestResolveException;
	
	/**
	 * 개점 하기
	 * @param param
	 * @throws RequestResolveException
	 */
	SingleMap sOpenInfo(SingleMap param) throws RequestResolveException;

	SingleMap saveStoreTemp() throws RequestResolveException;
	
	SvcStore getStoreById(Long id);

	// 앱버전 가져오기?
	SingleMap getAppVersion(ServletContext context, SingleMap param) throws RequestResolveException;

	// 특산품업체 정보 가져오기
	SingleMap getPluCategoriesSpecialtyInfo(SingleMap param);
}
