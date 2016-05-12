package com.execmobile.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Zone implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7199627639200075992L;
	private String zoneid;
	private Product product;
	private String name;
	private int bundleSize;

	public Zone() {
	}

	public Zone(String zoneid, Product product, String name, int bundleSize) {
		this.zoneid = zoneid;
		this.product = product;
		this.name = name;
		this.bundleSize = bundleSize;
	}

	public String getZoneid() {
		return this.zoneid;
	}

	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}

	@JsonIgnore
	public Product getProduct() {
		return this.product;
	}

	@JsonProperty
	public void setProduct(Product product) {
		this.product = product;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getBundleSize() {
		return this.bundleSize;
	}

	public void setBundleSize(int bundleSize) {
		this.bundleSize = bundleSize;
	}

}
