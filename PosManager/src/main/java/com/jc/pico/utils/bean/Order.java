/*
 * Filename	: Order.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.bean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.bean.*;

import java.util.List;
import java.util.Map;

public class Order {
  private SvcOrder order;
  private List<SvcOrderItem> items;
  private Map<Integer, SvcOrderItemOpt> options;
  private List<SvcOrderPay> pays;
  private List<SvcOrderDiscount> discounts;

  public SvcOrder getOrder() {
    return order;
  }

  public void setOrder(SvcOrder order) {
    this.order = order;
  }

  public List<SvcOrderItem> getItems() {
    return items;
  }

  public void setItems(List<SvcOrderItem> items) {
    this.items = items;
  }

  public void addItem(SvcOrderItem item) {
    if (this.items == null) {
      this.items = Lists.newArrayList();
    }
    items.add(item);
  }

  public Map<Integer,SvcOrderItemOpt> getOptions() {
    return options;
  }

  public void setOptions(Map<Integer, SvcOrderItemOpt> options) {
    this.options = options;
  }

  public void addOption(Integer ordinal, SvcOrderItemOpt option) {
    if (this.options == null) {
      this.options = Maps.newHashMap();
    }
    this.options.put(ordinal, option);
  }

  public List<SvcOrderPay> getPays() {
    return pays;
  }

  public void setPays(List<SvcOrderPay> pays) {
    this.pays = pays;
  }

  public void addPay(SvcOrderPay pay) {
    if (this.pays == null) {
      this.pays = Lists.newArrayList();
    }

    this.pays.add(pay);
  }

  public List<SvcOrderDiscount> getDiscounts() {
    return discounts;
  }

  public void setDiscounts(List<SvcOrderDiscount> discounts) {
    this.discounts = discounts;
  }

  public void addDiscount(SvcOrderDiscount discount) {
    if (this.discounts == null) {
      this.discounts = Lists.newArrayList();
    }
    this.discounts.add(discount);
  }
}
