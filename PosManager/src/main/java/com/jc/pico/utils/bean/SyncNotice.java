/*
 * Filename	: Mqtt.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.bean;

import java.util.Date;
import java.util.List;

public class SyncNotice {

  public static class From {
    private String type;
    private String no;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getNo() {
      return no;
    }

    public void setNo(String no) {
      this.no = no;
    }
  }

  public static class Order {
    private Long id;
    private String orderNo;
    private Date orderTm;
    private String orderTp;
    private Double sales;
    private Date reserveTm;
    private Date openDt;
    private Integer customerCnt;
    private Item item; // first item.

    
    public Date getOpenDt() {
      return openDt;
    }

    public void setOpenDt(Date openDt) {
      this.openDt = openDt;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getOrderNo() {
      return orderNo;
    }

    public void setOrderNo(String orderNo) {
      this.orderNo = orderNo;
    }

    public Date getOrderTm() {
      return orderTm;
    }

    public void setOrderTm(Date orderTm) {
      this.orderTm = orderTm;
    }

    public String getOrderTp() {
      return orderTp;
    }

    public void setOrderTp(String orderTp) {
      this.orderTp = orderTp;
    }

    public Double getSales() {
      return sales;
    }

    public void setSales(Double sales) {
      this.sales = sales;
    }

    public Date getReserveTm() {
      return reserveTm;
    }

    public void setReserveTm(Date reserveTm) {
      this.reserveTm = reserveTm;
    }

    public Integer getCustomerCnt() {
      return customerCnt;
    }

    public void setCustomerCnt(Integer customerCnt) {
      this.customerCnt = customerCnt;
    }

    public Item getItem() {
      return item;
    }

    public void setItem(Item item) {
      this.item = item;
    }
  }

  public static class Item {
    private Long id;
    private Double price;
    private Integer count;
    private String name;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public Double getPrice() {
      return price;
    }

    public void setPrice(Double price) {
      this.price = price;
    }

    public Integer getCount() {
      return count;
    }

    public void setCount(Integer count) {
      this.count = count;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class User {
    private Long id;
    private String name;
    private String tel;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getTel() {
      return tel;
    }

    public void setTel(String tel) {
      this.tel = tel;
    }
  }

  public interface Data {}
  public static class OrderData implements Data {
    private String status; // 주문 경로 타입(pos: 606001, app: 606002,  clerk: 606003, tab: 606004)
    private Order order;
    private User user;

    public String getStatus() {
      return status;
    }

    public void setStatus(String type) {
      this.status = type;
    }

    public Order getOrder() {
      return order;
    }

    public void setOrder(Order order) {
      this.order = order;
    }

    public User getUser() {
      return user;
    }

    public void setUser(User user) {
      this.user = user;
    }
  }

  public static class TableData implements Data {
    private String action;
    private List<String> origin;
    private List<String> target;

    public String getAction() {
      return action;
    }

    public void setAction(String action) {
      this.action = action;
    }

    public List<String> getOrigin() {
      return origin;
    }

    public void setOrigin(List<String> origin) {
      this.origin = origin;
    }

    public List<String> getTarget() {
      return target;
    }

    public void setTarget(List<String> target) {
      this.target = target;
    }
  }

  private String division;
  private From from;
  private Data data; // OrderData or TableData

  public String getDivision() {
    return division;
  }

  public void setDivision(String division) {
    this.division = division;
  }

  public From getFrom() {
    return from;
  }

  public void setFrom(From from) {
    this.from = from;
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }
}
