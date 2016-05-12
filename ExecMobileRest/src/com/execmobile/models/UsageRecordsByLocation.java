package com.execmobile.models;

import java.util.List;

public class UsageRecordsByLocation {

	List<String> countries;
	List<UsageByLocation> usageRecords;
	/**
	 * @return the countries
	 */
	public List<String> getCountries() {
		return countries;
	}
	/**
	 * @param countries the countries to set
	 */
	public void setCountries(List<String> countries) {
		this.countries = countries;
	}
	/**
	 * @return the usageRecords
	 */
	public List<UsageByLocation> getUsageRecords() {
		return usageRecords;
	}
	/**
	 * @param usageRecords the usageRecords to set
	 */
	public void setUsageRecords(List<UsageByLocation> usageRecords) {
		this.usageRecords = usageRecords;
	}
	
}
