package com.jc.pico.service.clerk;

import java.util.List;

import javax.servlet.ServletContext;

import com.jc.pico.bean.SvcKitchenPrinter;
import com.jc.pico.bean.SvcSalesList;
import com.jc.pico.bean.SvcStorePrinter;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.StoreParam;
import com.jc.pico.utils.bean.SvcOrderExtended;
import com.jc.pico.utils.bean.SvcTableExtended;

public interface ClerkOrderService {

	SvcOrderExtended getOrderDetailOrThrow(SingleMap param) throws RequestResolveException;

	SingleMap setTableCustomerCount(SingleMap param) throws RequestResolveException;

	List<SingleMap> moveTableOrder(SingleMap param) throws RequestResolveException;

	SvcTableExtended saveOrder(SingleMap param) throws RequestResolveException;
	
	// 추가 : 
	SvcTableExtended saveOrderKiosk(ServletContext context, SingleMap param) throws RequestResolveException;
	// 추가 : 

	SvcOrderExtended getOrderDetailOrThrowByOrderId(long orderId) throws RequestResolveException;

	SvcOrderExtended getOrderDetailByOrderId(long orderId);

	SvcOrderExtended getOrderDetail(SingleMap param);

	// 추가 : 
	SvcOrderExtended getOrderDetailEx(SingleMap param);
	SvcOrderExtended getOrderDetailByOrderIdEx(long storeId, long brandId, String orderId);
	
	SvcOrderExtended getOrderDetailKiosk(SingleMap param);
	//List<SvcSalesItemEx> getOrderDetailKioskEx(SingleMap param);
	//List<SvcSalesItem> getOrderDetailKioskEx(SingleMap param);
	SvcSalesList getOrderDetailKioskEx(SingleMap param);
	SvcOrderExtended getOrderDetailByOrderKiosk(long orderId);
	
	/**
	 * setOrderCancelComplete
	 * @param param
	 * @return
	 */
	public boolean setOrderCancelComplete(SingleMap param);
	
	/**
	 * getMerchantAndPrinter
	 * @param param
	 * @return
	 */
	public SvcStorePrinter getMerchantAndPrinter(SingleMap param);
	// 추가 : 

	
	SingleMap getTableDetail(SingleMap param) throws RequestResolveException;

	List<SingleMap> mergeTableOrder(SingleMap param) throws RequestResolveException;

	List<SvcKitchenPrinter> getOrderKitchenPrinter();

	List<SvcKitchenPrinter> setOrderKitchenPrinter(SingleMap param);

	SingleMap storeInfo(SingleMap param) throws RequestResolveException;

	SvcSalesList getSalesDetailKiosk(SingleMap param) throws RequestResolveException;
}
