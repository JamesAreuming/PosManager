package com.jc.pico.service.store;

import java.io.IOException;

import javax.naming.NoPermissionException;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jc.pico.exception.InvalidParamException;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.StoreParam;

public interface StoreCommonService {

	SingleMap getAppInfo(SingleMap param) throws InvalidParamException, RequestResolveException;

	/**
	 * 프랜차이즈의 브랜드 목록
	 * 
	 * @param param
	 *            franId : 프랜차이즈 ID
	 * @return
	 * 		id : 브랜드 ID
	 *         brandNm : 브랜드 명
	 * 
	 */
	SingleMap getBrandListByFranId(SingleMap param);

	/**
	 * 브랜드의 스토어 목록
	 * 
	 * @param param
	 *            brandId: brandId
	 * @return
	 */
	SingleMap getStoreListByBrandId(SingleMap param);

	/**
	 * 매장의 서비스 목록
	 * 
	 * @param param
	 *            storeId : 스토어ID
	 * @return
	 */
	SingleMap getStoreServiceListByStoreId(SingleMap param);

	/**
	 * 사용자 정보 조회
	 * 
	 * @param param
	 * @return
	 */
	SingleMap getUserDetail(SingleMap param);

	/**
	 * 사용자 디바이스 정보 저장, 푸시 토큰 저장
	 * 
	 * @param param
	 *            - os : android, ios (main_cd : 103)
	 *            - uuid : android_id or uuid (apple)
	 *            - deviceId : 서버에서 발급한 deviceId
	 *            - pushId : 푸시 토큰
	 *
	 * @return
	 * @throws NoPermissionException
	 */
	SingleMap addUserDevice(SingleMap param) throws NoPermissionException;

	/**
	 * 사용자 설정 조회
	 * 
	 * @param param
	 * @return
	 * @throws NoPermissionException
	 * @throws RequestResolveException
	 */
	SingleMap getUserSettings(SingleMap param) throws NoPermissionException, RequestResolveException;

	/**
	 * 사용자 설정 저장
	 * 
	 * @param param
	 * @return
	 * @throws NoPermissionException
	 * @throws RequestResolveException
	 */
	SingleMap setUserSettings(SingleMap param) throws NoPermissionException, RequestResolveException;

	/**
	 * 사용자 패스워드 변경
	 * 
	 * @param param
	 *            password : 현재 패스워드
	 *            newPassword : 새로운 패스워드
	 * @return
	 * @throws NoPermissionException
	 */
	SingleMap setUserPassword(SingleMap param) throws NoPermissionException;

	/**
	 * 디바이스와 연결된 사용자 (owner)를 변경 한다.
	 * 
	 * @param deviceId
	 * @param user
	 */
	void updateUserDeviceOwner(String deviceId, long userId);

	/**
	 * 브랜드의 지역 통화 소숫점 자리수
	 * brandId가 있으면 해당으로 조회.
	 * brandId가 없으면 storeId로 조회.
	 * storeId도 없으면 기본 0
	 * 
	 * @param param
	 *            storeId 상점 ID
	 *            brandId : 브랜드 번호
	 * @return
	 */
	int getBrandFractionDigits(SingleMap param);

	boolean uploadLogFile(MultipartHttpServletRequest req, MultipartFile[] files) throws IOException;

	boolean updateStorePrinterIp(SingleMap data);

}
