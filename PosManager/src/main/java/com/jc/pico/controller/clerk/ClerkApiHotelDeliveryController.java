package com.jc.pico.controller.clerk;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jc.pico.bean.Message;
import com.jc.pico.bean.MessageModel;
import com.jc.pico.bean.SvcDelivery;
import com.jc.pico.bean.SvcItemImg;
import com.jc.pico.bean.SvcOrderItem;
import com.jc.pico.bean.SvcOrderItemOpt;
import com.jc.pico.mapper.SvcDeliveryMapper;
import com.jc.pico.mapper.SvcItemImgMapper;
import com.jc.pico.mapper.SvcOrderItemMapper;
import com.jc.pico.mapper.SvcOrderItemOptMapper;
import com.jc.pico.utils.APIInit;
import com.jc.pico.utils.bean.SendMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Controller //일반 컨트롤러
@RequestMapping(value = "/order9/")
public class ClerkApiHotelDeliveryController {
	
	@Autowired
	SvcDeliveryMapper svcDeliveryMapper;
	
	@Autowired
	SvcOrderItemMapper svcOrderItemMapper;
	
	@Autowired
	SvcItemImgMapper svcItemImgMapper;
	
	@Autowired
	SvcOrderItemOptMapper svcOrderItemOptMapper;
	
	private static Logger logger = LoggerFactory.getLogger(ClerkApiHotelDeliveryController.class);
	
	/*
	 *  orderInfo - orderInfo, /modify, /orderNoSearch 
	 */

	
	/*
	 *  deliveryInfo - deliveryInfo, /register, /deliverySearch
	 */
	
	
	/*
	 * 주문상세정보 - GET
	 * : 주문번호, 영수번호, 상품이름, 수량, 가격, 총수량, 합계
	 * : 이름, 휴대전화 번호, 전화번호, 우편번호, 주소, 상세주소, 배송 메세지 
	 * 
	 */
	@RequestMapping(value = "orderInfo", method = RequestMethod.GET)
	public String orderInfo(Model model, @RequestParam("orderNo") String orderNo){
		// 제목
		String name = "주문 상세 정보";
		model.addAttribute("name", name);		
		
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
		
		List<SvcOrderItemOpt> orderItemOptInfo = svcOrderItemOptMapper.selectDeliveryOrderItemOptInfo(orderId);	
		model.addAttribute("orderItemOptInfo", orderItemOptInfo);
		
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
		// 제목
		String name = "배송 정보 수정";
		model.addAttribute("name", name);		
		//배송정보
		SvcDelivery customerInfo = svcDeliveryMapper.selectDeliveryCustomerInfo(orderNo);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date orderTm = customerInfo.getSvcOrder().getOrderTmLocal();
		String formatOrderDate = format.format(orderTm.getTime());
		model.addAttribute("formatOrderDate",formatOrderDate); //주문한날짜
		
		model.addAttribute("customerInfo", customerInfo);
	
		//상품
		Long orderId = customerInfo.getSvcOrder().getId();
		List<SvcOrderItem> orderItemInfo = svcOrderItemMapper.selectDeliveryOrderItemInfo(orderId);
		model.addAttribute("orderItemInfo",orderItemInfo);
			
		List<SvcOrderItemOpt> orderItemOptInfo = svcOrderItemOptMapper.selectDeliveryOrderItemOptInfo(orderId);	
		model.addAttribute("orderItemOptInfo", orderItemOptInfo);
		
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
	@RequestMapping(value = "orderInfo/orderNoSearch", method = RequestMethod.POST)
	public String orderInfoModifyRegiseter(HttpServletRequest request, SvcDelivery svcDelivery, Model model) throws IOException {
		String name = "주문 번호 조회";
		model.addAttribute("name", name);
		
		String orderNo = svcDelivery.getOrderNo();
		model.addAttribute("orderNo", orderNo);
		
		SvcDelivery beforeDeliveryInfo = svcDeliveryMapper.selectDeliveryCustomerInfo(orderNo);
		
		//과거정보
		String beforeName = beforeDeliveryInfo.getCusName();
		String beforeCellNo = beforeDeliveryInfo.getCusCellNo();
		System.out.println("과거"+beforeName); // 정아름
		System.out.println("과거"+beforeCellNo); //01042523245
		
		
		svcDeliveryMapper.updateDeliveryCustomerInfo(svcDelivery);

		
		SvcDelivery updateDeliveryInfo = svcDeliveryMapper.selectDeliveryCustomerInfo(orderNo);
		

		

		//현재정보
		String updateName = updateDeliveryInfo.getCusName();
		String updateCellNo = updateDeliveryInfo.getCusCellNo();
		
		System.out.println("업데이트"+beforeCellNo);
		
		//수신번호가 바꼈을 경우에만 수정 문자메세지 갈 수 있도록 구현
		if(!beforeCellNo.equals(updateCellNo)) {
			String host = String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort());
			sendMessage(updateName, updateCellNo, orderNo, host);
		} 
		
		System.out.println("POST : 정보수정후 -> search");
		return "/order9/orderInfoSearch";
	}
	
	private void sendMessage(String updateName, String updateCellNo, String orderNo, String host) throws IOException { 

		
		//주문상세정보
		String orderDetailInfo = host+"/order9/orderInfo?orderNo="+orderNo;
		
		
		//발신번호
		String order9Number = "0312030960";
		/*
		 * 안녕하세요 정아름님
		 * 입력하신 정보로
		 * 배달 정보가 수정되었습니다.
		 * 
		 * url
		 */
		String orderDeliveryInfoText = String.format("안녕하세요 %s님%n" // cusName
                                                      + "입력하신 정보로%n"
				                                      + "배달 정보가 수정되었습니다.%n%n"
                                                      
                                                      + "▼ 주문 상세 정보%n %s"
                                                      ,updateName , orderDetailInfo);
				
		//수신번호, 발신번호, 제목, 내용
		Message message = new Message(updateCellNo, order9Number, orderDeliveryInfoText, "배송 정보 수정");
		

		Call<MessageModel> api = APIInit.getAPI().sendMessage(APIInit.getHeaders(), message);
		 api.enqueue(new Callback<MessageModel>() { //MessageModel 추가
	            @Override
	            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
	                // 성공 시 200이 출력됩니다.
	                if (response.isSuccessful()) {
	                    System.out.println("statusCode : " + response.code());
	                    MessageModel body = response.body();
	                    System.out.println("groupId : " + body.getGroupId());
	                    System.out.println("messageId : " + body.getMessageId());
	                    System.out.println("to : " + body.getTo());
	                    System.out.println("from : " + body.getFrom());
	                    System.out.println("type : " + body.getType());
	                    System.out.println("statusCode : " + body.getStatusCode());
	                    System.out.println("statusMessage : " + body.getStatusMessage());
	                    System.out.println("customFields : " + body.getCustomFields());
	                } else {
	                    try {
	                        System.out.println(response.errorBody().string());
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }

	            @Override
	            public void onFailure(Call<MessageModel> call, Throwable throwable) {
	                throwable.printStackTrace();
	            }
	        });
	    }	
	
	/*
	 * 배송 정보 검색 - GET
	 */	
	@RequestMapping(value = "orderInfo/orderNoSearch", method = RequestMethod.GET)
	public String orderInfoSearch(Model model) {
		String name = "주문 번호 조회";
		model.addAttribute("name", name);
		System.out.println("GET : search");
		return "/order9/orderInfoSearch";
	}
	
	
	/*
	 * 배달 최종 정보
	 */
	
	@RequestMapping(value = "deliveryInfo", method = RequestMethod.GET)
	public String deliveryInfo(Model model) {
		String name = "주문 상세 정보";
		model.addAttribute("name", name);
		return "/order9/deliveryInfo";
	}
	
	/*
	 * 택배사, 송장번호 등록
	 */
	@RequestMapping(value = "deliveryInfo/register", method = RequestMethod.GET)
	public String deliveryInfoRegister(Model model) {
		String name = "택배사 & 송장번호 등록";
		model.addAttribute("name", name);
		
		return "/order9/deliveryInfoRegister";
	}
	
	/*
	 * 검색
	 */
//	@RequestMapping(value = "deliveryInfo/deliverySearch", method = RequestMethod.POST)
//	public String deliveryInfoSearchRegister() {
//		
//		return "/order9/deliveryInfoSearch";
//	}	
	
	/*
	 * 검색
	 */	
	
	@RequestMapping(value = "deliveryInfo/deliverySearch", method = RequestMethod.GET)
	public String deliveryInfoSearch(Model model) {
		String name = "주문 번호 조회";
		model.addAttribute("name", name);
		
		return "/order9/deliveryInfoSearch";
	}
}
