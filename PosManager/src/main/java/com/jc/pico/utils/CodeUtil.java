/*
 * Filename	: CodeUtil.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.bean.BaseBcode;
import com.jc.pico.bean.BaseBcodeExample;
import com.jc.pico.mapper.BaseBcodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ruinnel on 2016. 4. 12..
 */
@Component
//@Scope("singleton") // default?
public class CodeUtil implements InitializingBean {
  protected static Logger logger = LoggerFactory.getLogger(CodeUtil.class);
  
  @Autowired
  private BaseBcodeMapper mapper;

  private Map<String, BaseBcode> codeMap;
  private Map<String, BaseBcode> aliasMap;
  private Map<String, List<BaseBcode>> mcodeMap;

  private long mLastUpdated = 0;

  @Override
  public void afterPropertiesSet() throws Exception {
    reload();
  }

  public void reload() {
    // clear maps
    codeMap = Maps.newHashMap();
    aliasMap = Maps.newHashMap();
    mcodeMap = Maps.newHashMap();

    BaseBcodeExample example = new BaseBcodeExample();
    example.createCriteria().andUseYnEqualTo(true);
    List<BaseBcode> codes = mapper.selectByExample(example); // get all

    for (int i = 0; (codes != null && i < codes.size()); i++) {
      BaseBcode code = codes.get(i);
      codeMap.put(code.getBaseCd(), code);
      aliasMap.put(code.getAlias(), code);

      if (mLastUpdated == 0 || mLastUpdated < code.getUpdated().getTime()) {
        mLastUpdated = code.getUpdated().getTime();
      }

      List<BaseBcode> mcodes = mcodeMap.get(code.getMainCd());
      if (mcodes == null) {
        mcodes = Lists.newArrayList();
      }
      mcodes.add(code);
      mcodeMap.put(code.getMainCd(), mcodes);
    }
  }

  public BaseBcode getByCode(String code) {
    return codeMap.get(code);
  }

  public BaseBcode getByAlias(String alias) {
    return aliasMap.get(alias);
  }

  public String getBaseCodeByAlias(String alias) {
    BaseBcode code = getByAlias(alias);
    return (code != null ? code.getBaseCd() : "");
  }

  public String getTitleByCode(String cd) {
    BaseBcode code = getByCode(cd);
    return (code != null ? code.getTitle() : "");
  }

  public String getTitleByAlias(String alias) {
    BaseBcode code = getByAlias(alias);
    return (code != null ? code.getTitle() : "");
  }

  public String getAliasByCode(String cd) {
    BaseBcode code = getByCode(cd);
    return (code != null ? code.getAlias() : "");
  }

  public List<BaseBcode> getByMainCode(String mainCode) {
    return mcodeMap.get(mainCode);
  }

  public long getLastUpdated() {
    return mLastUpdated;
  }

  public List<BaseBcode> getAll() {
    Collection<BaseBcode> values = codeMap.values();
    return Lists.newArrayList(values.iterator());
  }
}
