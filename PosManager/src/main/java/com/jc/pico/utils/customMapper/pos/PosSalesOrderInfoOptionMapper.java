package com.jc.pico.utils.customMapper.pos;

import java.util.List;
import java.util.Map;

import com.jc.pico.utils.bean.PosSalesOrderInfoOption;

public interface PosSalesOrderInfoOptionMapper {

	List<PosSalesOrderInfoOption> selectDefault(Map<String, Object> params);

}
