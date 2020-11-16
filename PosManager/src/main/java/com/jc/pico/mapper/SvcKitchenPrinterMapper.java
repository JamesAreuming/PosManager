package com.jc.pico.mapper;

import java.util.HashMap;
import java.util.List;

import com.jc.pico.bean.SvcKitchenPrinter;

public interface SvcKitchenPrinterMapper {

	int insertList(List<SvcKitchenPrinter> svcKitchenPrintList);

	List<SvcKitchenPrinter> selectList(SvcKitchenPrinter svcKitchenPrinter);

	void update(SvcKitchenPrinter svcKitchenPrinter);

	void deleteKitchenPrinterAtOrderCancel(HashMap<String, Object> cancelOrder);
}
