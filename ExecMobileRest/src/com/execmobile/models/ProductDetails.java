/**
 * 
 */
package com.execmobile.models;

import java.util.List;

import com.execmobile.data.Country;
import com.execmobile.data.Productpriceplan;

/**
 * @author Nakul's Dev Machine
 *
 */
public class ProductDetails {
	
	private Productpriceplan productPricePlan;
	private List<Country> productCoverage;
	/**
	 * @return the productPricePlan
	 */
	public Productpriceplan getProductPricePlan() {
		return productPricePlan;
	}
	/**
	 * @param productPricePlan the productPricePlan to set
	 */
	public void setProductPricePlan(Productpriceplan productPricePlan) {
		this.productPricePlan = productPricePlan;
	}
	/**
	 * @return the productCoverage
	 */
	public List<Country> getProductCoverage() {
		return productCoverage;
	}
	/**
	 * @param productCoverage the productCoverage to set
	 */
	public void setProductCoverage(List<Country> productCoverage) {
		this.productCoverage = productCoverage;
	}
	
	

}
