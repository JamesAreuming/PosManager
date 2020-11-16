/*
 * data.put("Filename", ""); // : ApiUtil.java
 * data.put("Function", ""); // :
 * Comment 	:
 * data.put("History", ""); // : 
 *
 * data.put("Version", ""); // : 1.0
 * Author   : 
 */

package com.jc.pico.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderItem;
import com.jc.pico.bean.SvcOrderPay;
import com.jc.pico.bean.User;
import com.jc.pico.configuration.Globals;
import com.jc.pico.exception.OAuth2LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ApiUtil {
  protected static Logger logger = LoggerFactory.getLogger(ApiUtil.class);

  public static Map<String, String> login(String grantType, String clientId, String username, String password, String deviceId) throws OAuth2LoginException {
    try {
      Map<String, String> params = Maps.newHashMap();
      params.put("grant_type", grantType);
      params.put("client_id", clientId);
      params.put("username", username);
      params.put("password", password);
      params.put("deviceId", deviceId);

      String url = Globals.LOGIN_URL;
      String response = HttpUtil.request(url, params);
      Map<String, String> oauth = JsonConvert.JsonConvertObject(response, new TypeReference<Map<String, String>>() {});

      return oauth;
    } catch (Exception e) {
      logger.warn("login fail", e);
      throw new OAuth2LoginException(e.getMessage());
    }
  }

  public static void sendOrder(User user, SvcOrder order, List<SvcOrderItem> items, List<SvcOrderPay> payment) {
    // TODO send to POS here
    try {
      //String url = "https://transmit.solbipos.co.kr/Api/SOLBI/WebServiceOrderAPI.asmx/OrderApi"; // 운영
      String url = "http://122.35.116.74:8888/Api/SOLBI/WebServiceOrderAPI.asmx/OrderApi";
//      String url = "http://122.35.116.74:8888/Api/TEST_BBQ_API.aspx";
      SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
      Map<String, Object> data = Maps.newHashMap();

      data.put("comp_code", "JCSDE"); // 회사코드(솔비포스에서 부여 받은 고정값) TODO
      data.put("client_id", "fyf6sRIlR4b2qQ");  // 클라이언트ID(솔비포스에서 부여 받은 고정값) TODO
      data.put("order_datetime", format.format(order.getOrderTm())); // 주문발생(예약)일시
      data.put("gubun", 1); // 주문 구분
      data.put("client_type", 8); // 클라이언트 구분
      data.put("order_type", 8); // 주문 형태 // 3: 예약?
      data.put("order_trans_no", String.format("%d", order.getId())); // 주문 거래 번호(주문 고유 Key)
      data.put("store_code", "00001"); // 매장코드 TODO
      data.put("pos_no", "1");
      data.put("order_amt", order.getSales().intValue()); // 주문 금액
      data.put("order_dc", order.getDiscount().intValue()); // 주문 할인 금액
      data.put("order_memo", order.getMemo()); // 주문 메모
      data.put("tel_no", user.getTel()); // 전화번호
      data.put("mobile_no", user.getMb()); // 핸드폰번호
      data.put("addr1", user.getAddr1()); // 배달 주소1
      data.put("addr2", user.getAddr2()); // 배달 주소2
      data.put("addr3", ""); // 배달 주소3
      data.put("order_qty", 1); // ??
      data.put("customer_name", user.getName()); // 고객명
      data.put("customer_type", 1); // 고객형태 // 1: 친구
      data.put("customer_age", 40); // 고객연령대 // 3: 30대
      data.put("customer_sex", 1); // 고객성별 // 1: 남
      data.put("customer_cnt", 1); // 고객수
      data.put("table_no", 1);// order.getTableNo()); // 테이블번호
      data.put("pay_type", 1); // 지불 결제 형태 // 1: 현금 2: 신용카드
      data.put("prepaid", true); // 선결제여부
      data.put("status", "1"); // 주문상태 1 : 신규 주문
      data.put("remark", ""); // 비고
      data.put("add_data1", ""); // 추가정보1
      data.put("add_data2", ""); // 추가정보2
      data.put("add_data3", ""); // 추가정보3

      // 주문 상세 처리
      List<Map<String, Object>> details = Lists.newArrayList();
      for (int i = 0; (items != null && i < items.size()); i++) {
        SvcOrderItem item = items.get(i);
        Map<String, Object> detail = Maps.newHashMap();

        //logger.debug(JsonConvert.toJson(item));
        detail.put("seq", i + 1); // 순번
        detail.put("goods_code", item.getItemCd()); // 품목코드
        detail.put("goods_name", item.getItemCd()); // 품명
        detail.put("sale_price", item.getPrice().intValue()); // 단가
        detail.put("order_qty", item.getCount()); // 주문수량
        detail.put("order_dc", item.getDiscount().intValue()); // 할인금액
        detail.put("order_amt", (item.getPrice().intValue() * item.getCount()) - item.getDiscount().intValue()); // 주문금액
        detail.put("order_memo", ""); // 메모

        details.add(detail);
      }
      data.put("details", details); // 주문 상세

      String response = HttpUtil.request(url, JsonConvert.toJson(data));

      logger.info("SOLBI(send order response) = " + response);
    } catch (Exception e) {
      logger.warn("send order  fail", e);
    }
  }

  public static void main(String[] args) {
    List<SvcOrderItem> items = Lists.newArrayList();
    List<SvcOrderPay> payment = Lists.newArrayList();

    Date now = new Date();

    // make user
    User user = new User();
    user.setName("김도형");
    user.setAddr1("강동구 암사동");
    user.setAddr1("500-111");
    user.setTel("111-2222-3333");
    user.setMb("010-1111-2222");

    // make item
    SvcOrderItem item = new SvcOrderItem();
    item.setOrderTm(now);
    item.setItemNm("Americano");
    item.setItemCd("AM");
    item.setCatNm("Coffe");
    item.setCatCd("COF");
    item.setPrice(3000.0);
    item.setCount(Short.valueOf((short) 1));
    item.setDiscount(0.0);
    items.add(item);

    // make payment
    SvcOrderPay pay = new SvcOrderPay();
    pay.setPayTm(now);
    pay.setAmount(3000.0);
    pay.setPayMethod("card");
    payment.add(pay);

    // make order
    SvcOrder order = new SvcOrder();
    order.setBrandId(4l);
    order.setStoreId(1l);
    order.setUserId(2l);
    order.setOpenDt(now);
    order.setOrderTm(now);
    order.setSales(3000.0);
    order.setDiscount(0.0);
    order.setSupplyValue(1000.0);
    order.setMemo("");
    order.setServiceCharge(0.0);
    order.setTax(0.0);
    sendOrder(user, order, items, payment);
  }
}
