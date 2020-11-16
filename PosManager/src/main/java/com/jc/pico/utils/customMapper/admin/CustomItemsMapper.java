/*
 * Filename	: CustomStoreMapper.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.customMapper.admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jc.pico.bean.SvcItem;
import com.jc.pico.bean.SvcItemCat;
import com.jc.pico.bean.SvcItemImg;
import com.jc.pico.bean.SvcPluCat;
import com.jc.pico.bean.SvcPluItem;

public interface CustomItemsMapper {
	
 /**
  *
  * @param itemCat
  * @return
  */
  List<Map<String, Object>> getItemCategory(SvcItemCat itemCat);

  /**
   *
   * @param itemCat
   * @return
   */
  List<Map<String, Object>> getItemCateSelect(SvcItemCat itemCat);

  /**
   *
   * @param id
   * @return
   */
  SvcItemImg getItemImgId(Long id);

  /**
   * pos-plu category
   * @param pluCat
   * @return
   */
  List<Map<String, Object>> getPluCategory(SvcPluCat pluCat);

  /**
   * pluTypeSelect
   * @param pluCat
   * @return
   */
  List<Map<String, Object>> pluTypeSelect(SvcPluCat pluCat);

  /**
   * getPluItemGridly
   * @param svcPluItem
   * @return
   */
  List<Map<String, Object>> getPluItemGridly(SvcPluItem svcPluItem);


  /**
   * getItemInfo
   * @param item
   * @param rowBounds
   * @return
   */
  List<Map<String, Object>> getItemInfo(Map<String, String> search);

  /**
   * getItemCount
   * @param search
   * @return
   */
  int getItemCount(Map<String, String> search);


  /**
   *
   * @param item
   * @return
   */
  List<LinkedHashMap<String, Object>> selectItems(SvcItem item);


}
