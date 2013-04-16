package com.paradigmcreatives.sqliteproduct;

public class ProductModel {
	public String idno = "", productname = "", productprice = "";

	public String getProductname() {
		return productname;

	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getProductprice() {
		return productprice;
	}

	public void setProductprice(String productprice) {
		this.productprice = productprice;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

}
