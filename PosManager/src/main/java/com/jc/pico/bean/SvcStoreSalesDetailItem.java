package com.jc.pico.bean;

import java.util.List;

public class SvcStoreSalesDetailItem {
	private Long id;
	private String itemNm;				//상품명
	private Double price;				//판매가격
	private int count;						//상품 수량
	private Double optPrice;			//옵션 가격
	private Double sales;				//매출
	private Double tax;					//세금
	private Double netSales;			//순 매출
	private String smallImage;		//작은 이미지 URL
	private String smallImageView;	//작은 이미지 명
	private String image;				//원본 이미지 URL
	private String imageView;			//원본 이미지 명
	private List<SvcStoreSalesDetailItemDtl> itemDtls;
	public Long getId() {
		return id;
	}
	public String getItemNm() {
		return itemNm;
	}
	public Double getPrice() {
		return price;
	}
	public int getCount() {
		return count;
	}
	public Double getOptPrice() {
		return optPrice;
	}
	public Double getSales() {
		return sales;
	}
	public Double getTax() {
		return tax;
	}
	public Double getNetSales() {
		return netSales;
	}
	public String getSmallImage() {
		return smallImage;
	}
	public String getSmallImageView() {
		return smallImageView;
	}
	public String getImage() {
		return image;
	}
	public String getImageView() {
		return imageView;
	}
	public List<SvcStoreSalesDetailItemDtl> getItemDtls() {
		return itemDtls;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setOptPrice(Double optPrice) {
		this.optPrice = optPrice;
	}
	public void setSales(Double sales) {
		this.sales = sales;
	}
	public void setTax(Double tax) {
		this.tax = tax;
	}
	public void setNetSales(Double netSales) {
		this.netSales = netSales;
	}
	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}
	public void setSmallImageView(String smallImageView) {
		this.smallImageView = smallImageView;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setImageView(String imageView) {
		this.imageView = imageView;
	}
	public void setItemDtls(List<SvcStoreSalesDetailItemDtl> itemDtls) {
		this.itemDtls = itemDtls;
	}
	@Override
	public String toString() {
		return "SvcStoreSalesDetailItem [id=" + id + ", itemNm=" + itemNm + ", price=" + price + ", count=" + count
				+ ", optPrice=" + optPrice + ", sales=" + sales + ", tax=" + tax + ", netSales=" + netSales
				+ ", smallImage=" + smallImage + ", smallImageView=" + smallImageView + ", image=" + image
				+ ", imageView=" + imageView + ", itemDtls=" + itemDtls + "]";
	}
}
