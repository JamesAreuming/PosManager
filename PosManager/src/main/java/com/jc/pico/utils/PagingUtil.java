/*
 * Filename	: PagingUtil.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagingUtil {
  private static final int ROWS_PER_PAGE = 10;
  private static final int PAGE_NO = 1;

  public static Map<String, Object> setDefaultValuesForList(List<Object> items, int totalCnt, Map<String, String> searchOpts) {
    return setDefaultValuesForList(items, totalCnt, ROWS_PER_PAGE, PAGE_NO, searchOpts);
  }
  
  public static Map<String, Object> setDefaultValuesForList(List<Object> items, int totalCnt, int pageNo, Map<String, String> searchOpts) {
    return setDefaultValuesForList(items, totalCnt, ROWS_PER_PAGE, pageNo, searchOpts);
  }

  public static Map<String, Object> setDefaultValuesForList(List<Object> items, int totalCnt, int rowsPerPage, int pageNo, Map<String, String> searchOpts) {
    HashMap<String, Object> result = new HashMap<>();
    result.put("items", items);
    result.put("totalCnt", totalCnt);
    result.put("rowsPerPage", rowsPerPage);
    result.put("pageNo", pageNo);
    result.put("searchOptions", searchOpts);

    return result;
  }
}
