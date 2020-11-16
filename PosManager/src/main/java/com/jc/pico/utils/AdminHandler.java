/**
 * <pre>
 * Filename	: AdminHandler.java
 * Function	: Admin 공통 데이터 처리
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 * </pre>
 */
package com.jc.pico.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class AdminHandler {

	/**
	 * result map에 세션 정보 세팅
	 * 
	 * @param orgMap
	 * @param session
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	public static Map<Object, Object> setSessionInfo(Map<Object, Object> orgMap, HttpSession session, String menuId) throws Exception {
		// get user info.
		HashMap<String, Object> userInfoMap = AdminHandler.getUserInfo(session);
		// get user auth.
		HashMap<String, Object> userAuthMap = AdminHandler.getUserAuth(session, menuId);
		
		// set session info.
		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("userInfo", userInfoMap);
		sessionMap.put("userAuth", userAuthMap);
		
		// set result map
		orgMap.put("session", sessionMap);
		
		return orgMap;
	}
	
	/**
	 * 세션의 사용자 정보
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getUserInfo(HttpSession session) throws Exception {
		HashMap<String, Object> result = (HashMap<String, Object>)session.getAttribute("userInfo");
		
		return result;
	}
	
	/**
	 * 세션의 사용자 정보 - 사용자유형
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static String getUserType(HttpSession session) throws Exception {
		String result = "";
		HashMap<String, Object> map = AdminHandler.getUserInfo(session);
		if (map != null) {
			result = (String)map.get("USER_TYPE");
		}
		
		return result;
	}
	
	/**
	 * 세션의 전체 메뉴 권한
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getUserAuthAll(HttpSession session) throws Exception {
		HashMap<String, Object> result = (HashMap<String, Object>)session.getAttribute("userAuth");
		
		return result;
	}
	
	/**
	 * 세션의 메뉴 권한
	 * 
	 * @param session
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getUserAuth(HttpSession session, String menuId) throws Exception {
		HashMap<String, Object> authAllMap = AdminHandler.getUserAuthAll(session);
		if (authAllMap == null) {
			return null;
		}
		
		HashMap<String, Object> authMap = (HashMap<String, Object>)authAllMap.get(menuId);
		
		return authMap;
	}
	
	/**
	 * 메뉴 조회 권한
	 * 
	 * @param session
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static boolean isReadAuth(HttpSession session, String menuId) throws Exception {
		boolean result = false;
		
		// get auth map
		HashMap<String, Object> authMap = AdminHandler.getUserAuth(session, menuId);
		if (authMap == null) {
			return false;
		}
		
		// get menu auth map
		HashMap<String, String> menuMap = (HashMap<String, String>)authMap.get(menuId);
		if (menuMap == null) {
			return false;
		}
		
		String auth = (String)menuMap.get("READ_YN");
		if ("1".equals(auth)) {
			result = true;
		}
		
		return result;
	}
}
