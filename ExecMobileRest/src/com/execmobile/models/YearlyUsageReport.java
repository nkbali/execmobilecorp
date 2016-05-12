package com.execmobile.models;

import java.util.List;

public class YearlyUsageReport {
	
	List<String> months;
	List<YearlyUsage> usageRecords;
	/**
	 * @return the months
	 */
	public List<String> getMonths() {
		return months;
	}
	/**
	 * @param months the months to set
	 */
	public void setMonths(List<String> months) {
		this.months = months;
	}
	/**
	 * @return the usageRecords
	 */
	public List<YearlyUsage> getUsageRecords() {
		return usageRecords;
	}
	/**
	 * @param usageRecords the usageRecords to set
	 */
	public void setUsageRecords(List<YearlyUsage> usageRecords) {
		this.usageRecords = usageRecords;
	}

	
}
