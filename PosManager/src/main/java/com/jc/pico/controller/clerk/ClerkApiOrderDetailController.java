package com.jc.pico.controller.clerk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jc.pico.bean.SvcDelivery;
import com.jc.pico.bean.SvcItemImg;
import com.jc.pico.bean.SvcOrderItem;
import com.jc.pico.mapper.SvcDeliveryMapper;
import com.jc.pico.mapper.SvcItemImgMapper;
import com.jc.pico.mapper.SvcOrderItemMapper;

@Controller //일반 컨트롤러
@RequestMapping(value = "/order9/")
public class ClerkApiOrderDetailController {
	
	@Autowired
	SvcDeliveryMapper svcDeliveryMapper;
	
	@Autowired
	SvcOrderItemMapper svcOrderItemMapper;
	
	@Autowired
	SvcItemImgMapper svcItemImgMapper;
	
	private static Logger logger = LoggerFactory.getLogger(ClerkApiOrderDetailController.class);
	
	/*
	 * 주문상세정보 - GET
	 * : 주문번호, 영수번호, 상품이름, 수량, 가격, 총수량, 합계
	 * : 이름, 휴대전화 번호, 전화번호, 우편번호, 주소, 상세주소, 배송 메세지 
	 * 
	 */
	@RequestMapping(value = "orderInfo", method = RequestMethod.GET)
	public String orderInfo(Model model, @RequestParam("orderNo") String orderNo){	
		// 배송정보 + 주문한날짜
		SvcDelivery customerInfo = svcDeliveryMapper.selectDeliveryCustomerInfo(orderNo);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date orderTm = customerInfo.getSvcOrder().getOrderTmLocal();
		String formatOrderDate = format.format(orderTm.getTime());
		model.addAttribute("formatOrderDate",formatOrderDate); //주문한날짜
		
		model.addAttribute("customerInfo", customerInfo); //배송정보
		

	
		// 상품정보
		Long orderId = customerInfo.getSvcOrder().getId();
		List<SvcOrderItem> orderItemInfo = svcOrderItemMapper.selectDeliveryOrderItemInfo(orderId);
		model.addAttribute("orderItemInfo",orderItemInfo); //상품정보
		
		// 상품 총 합계
		int totalCount = 0;
		for(int i=0;i<orderItemInfo.size();i++) {
			int count = orderItemInfo.get(i).getCount();
			totalCount = totalCount + count;
		}
		model.addAttribute("totalCount", totalCount); // 상품 총 합계
		
		//상품 이미지
		List<SvcItemImg> orderItemImg = new ArrayList<SvcItemImg>();
		for(int i=0;i<orderItemInfo.size();i++) {
			Long itemId = orderItemInfo.get(i).getItemId();
			orderItemImg.addAll(i, svcItemImgMapper.selectDeliveryItemImg(itemId));				
		}
		model.addAttribute("orderItemImg", orderItemImg); // 상품 이미지

		return "/order9/orderInfo";
	}
	
	
	/*
	 * 배송 정보 수정(READ) - GET
	 */
	@RequestMapping(value = "orderInfo/modify", method = RequestMethod.GET)
	public String orderInfoModifyRead(Model model, @RequestParam("orderNo") String orderNo) {
		
		//배송정보
		SvcDelivery customerInfo = svcDeliveryMapper.selectDeliveryCustomerInfo(orderNo);
		model.addAttribute("customerInfo", customerInfo);
	
		//상품
		Long orderId = customerInfo.getSvcOrder().getId();
		List<SvcOrderItem> orderItemInfo = svcOrderItemMapper.selectDeliveryOrderItemInfo(orderId);
		model.addAttribute("orderItemInfo",orderItemInfo);
		
		//상품 총 합계
		int totalCount = 0;
		for(int i=0;i<orderItemInfo.size();i++) {
			int count = orderItemInfo.get(i).getCount();
			totalCount = totalCount + count;
		}
		model.addAttribute("totalCount", totalCount);
		
		//상품 이미지
		List<SvcItemImg> orderItemImg = new ArrayList<SvcItemImg>();
		for(int i=0;i<orderItemInfo.size();i++) {
			Long itemId = orderItemInfo.get(i).getItemId();
			orderItemImg.addAll(i, svcItemImgMapper.selectDeliveryItemImg(itemId));				
		}
		model.addAttribute("orderItemImg", orderItemImg);
		
		return "/order9/orderInfoModify";
	}
	
	/*
	 * 배송 정보 수정(REIGSTER) - POST
	 */	
	@RequestMapping(value = "orderInfo/modify", method = RequestMethod.POST)
	public String orderInfoModifyRegiseter() {
		
		return "/order9/orderInfo";
	}
	
}
