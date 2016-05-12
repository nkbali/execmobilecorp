package com.execmobile.helpers;

import java.lang.reflect.Method;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import com.execmobile.models.UsageByLocation;
import com.execmobile.models.UsageRecordsByLocation;
import com.execmobile.models.YearlyUsage;
import com.execmobile.models.YearlyUsageReport;

public class ReportParser {

	@SuppressWarnings("rawtypes")
	public YearlyUsageReport parseYearlyUsage(List yearlyUsageRecords, int year) {
		try {
			Iterator yearlyUsageRecordsIterator = yearlyUsageRecords.iterator();
			List<String> months = new ArrayList<String>();
			List<YearlyUsage> usageRecords = new ArrayList<YearlyUsage>();
			YearlyUsageReport usageReport = new YearlyUsageReport();
			for (int i = 1; i < 13; i++) {
				if (i < 10)
					months.add(year + "0" + i);
				else
					months.add(year + "" + i);
			}

			usageReport.setMonths(months);

			while (yearlyUsageRecordsIterator.hasNext()) {
				YearlyUsage usageRecord;
				Map yearlyUsageRecord = (Map) yearlyUsageRecordsIterator.next();
				Optional<YearlyUsage> existingRecord = usageRecords.stream()
						.filter(usage -> usage.getDeviceID().equals(yearlyUsageRecord.get("DeviceID").toString()))
						.findFirst();
				if (!existingRecord.isPresent()) {
					usageRecord = new YearlyUsage();
					usageRecord.setIsActive(Boolean.parseBoolean(yearlyUsageRecord.get("IsActive").toString()));
					usageRecord.setDeviceID(yearlyUsageRecord.get("DeviceID").toString());
					usageRecord.setProduct(yearlyUsageRecord.get("Name").toString());
					usageRecord.setCompany(yearlyUsageRecord.get("Company").toString());
				} else {
					usageRecord = existingRecord.get();
					usageRecords.remove(usageRecord);
				}

				String usageMonth = yearlyUsageRecord.get("UsageMonth").toString();
				int monthlyUsage = (int)Double.parseDouble(yearlyUsageRecord.get("MonthlyTotal").toString());
				usageMonth = usageMonth.replace("" + year, "");
				usageMonth = usageMonth.replace("0", "");
				int currentUsageMonth = Integer.parseInt(usageMonth);
				String currentUsageField = "usage" + currentUsageMonth;
				
				Method[] test = usageRecord.getClass().getMethods();
				for(Method method: test)
				{
					if(method.getName().equalsIgnoreCase("set"+currentUsageField))
					{
						method.invoke(usageRecord, monthlyUsage);
					}
				}
				
				int totalUsage = usageRecord.getTotal();
				totalUsage += monthlyUsage;
				usageRecord.setTotal(totalUsage);
				usageRecords.add(usageRecord);
			}
			
			YearlyUsage totalUsageRecord = new YearlyUsage();
			totalUsageRecord.setDeviceID("Total");
			int totalUsage1 = 0;
			int totalUsage2 = 0;
			int totalUsage3 = 0;
			int totalUsage4 = 0;
			int totalUsage5 = 0;
			int totalUsage6 = 0;
			int totalUsage7 = 0;
			int totalUsage8 = 0;
			int totalUsage9 = 0;
			int totalUsage10 = 0;
			int totalUsage11 = 0;
			int totalUsage12 = 0;
			int totalUsage = 0;
			for(YearlyUsage usageRecord : usageRecords)
			{
				totalUsage1 += usageRecord.getUsage1();
				totalUsage2 += usageRecord.getUsage2();
				totalUsage3 += usageRecord.getUsage3();
				totalUsage4 += usageRecord.getUsage4();
				totalUsage5 += usageRecord.getUsage5();
				totalUsage6 += usageRecord.getUsage6();
				totalUsage7 += usageRecord.getUsage7();
				totalUsage8 += usageRecord.getUsage8();
				totalUsage9 += usageRecord.getUsage9();
				totalUsage10 += usageRecord.getUsage10();
				totalUsage11 += usageRecord.getUsage11();
				totalUsage12 += usageRecord.getUsage12();
				totalUsage += usageRecord.getTotal();
			}
			
			totalUsageRecord.setUsage1(totalUsage1);
			totalUsageRecord.setUsage2(totalUsage2);
			totalUsageRecord.setUsage3(totalUsage3);
			totalUsageRecord.setUsage4(totalUsage4);
			totalUsageRecord.setUsage5(totalUsage5);
			totalUsageRecord.setUsage6(totalUsage6);
			totalUsageRecord.setUsage7(totalUsage7);
			totalUsageRecord.setUsage8(totalUsage8);
			totalUsageRecord.setUsage9(totalUsage9);
			totalUsageRecord.setUsage10(totalUsage10);
			totalUsageRecord.setUsage11(totalUsage11);
			totalUsageRecord.setUsage12(totalUsage12);
			totalUsageRecord.setTotal(totalUsage);
			usageRecords.add(totalUsageRecord);
			
			usageReport.setUsageRecords(usageRecords);
			return usageReport;
		} catch (Exception ex) {
				ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes" })
	public UsageRecordsByLocation parseLocationBasedUsage(List locationBasedUsageRecords) {
		UsageRecordsByLocation usageRecordsByLocation = new UsageRecordsByLocation();
		Set<String> countries = new HashSet<String>();
		List<UsageByLocation> usageRecords = new ArrayList<>();
		Iterator recordsIterator = locationBasedUsageRecords.iterator();
		
		while(recordsIterator.hasNext())
		{
			Map locationBasedUsageRecord = (Map) recordsIterator.next();
			String countryName = locationBasedUsageRecord.get("Location").toString();
			if(!countries.contains(countryName))
				countries.add(countryName);
		
		}
		List<String> countriesList = new ArrayList<String>(countries);
		Collections.sort(countriesList, Collator.getInstance());
		usageRecordsByLocation.setCountries(countriesList);
		
		recordsIterator = locationBasedUsageRecords.iterator();
		
		while(recordsIterator.hasNext())
		{
			Map locationBasedUsageRecord = (Map) recordsIterator.next();
			UsageByLocation usageRecord;
			Optional<UsageByLocation> existingRecord = usageRecords.stream()
					.filter(usage -> usage.getDeviceId().equals(locationBasedUsageRecord.get("DeviceID").toString()))
					.findFirst();
			TreeMap<String, Integer> usageMapByCountry;
			if (!existingRecord.isPresent()) {
				usageRecord = new UsageByLocation();
				usageMapByCountry = new TreeMap<String, Integer>();
				usageRecord.setIsActive(Boolean.parseBoolean(locationBasedUsageRecord.get("IsActive").toString()));
				usageRecord.setDeviceId(locationBasedUsageRecord.get("DeviceID").toString());
				usageRecord.setProduct(locationBasedUsageRecord.get("Name").toString());
				usageRecord.setCompany(locationBasedUsageRecord.get("Company").toString());
				usageRecord.setUsage(usageMapByCountry);
				
			} else {
				usageRecord = existingRecord.get();
				usageMapByCountry = usageRecord.getUsage();
				usageRecords.remove(usageRecord);
				usageMapByCountry = usageRecord.getUsage();
			}
			int total = usageRecord.getTotal();
			for(String country : countriesList)
			{
				if(usageMapByCountry.containsKey(country))
					continue;
				String countryName = locationBasedUsageRecord.get("Location").toString();
				if(country.equals(countryName))
				{
					int usage = (int)Double.parseDouble(locationBasedUsageRecord.get("Total").toString());
					total += usage;
					usageMapByCountry.put(country, usage);
				}
				
			}
			
			usageRecord.setUsage(usageMapByCountry);
			usageRecord.setTotal(total);
			usageRecords.add(usageRecord);
		}
		
		UsageByLocation totalUsageRecord = new UsageByLocation();
		totalUsageRecord.setDeviceId("Total");
		int totalUsageOverall = 0;
		TreeMap<String, Integer> totalUsageMapByCountry = new TreeMap<String, Integer>();
		for(String country : countriesList)
		{
			int totalUsageInCountry = 0;
			for(UsageByLocation usageRecord : usageRecords){
				TreeMap<String, Integer> usageMapForDevice = usageRecord.getUsage();
				if(usageMapForDevice.containsKey(country)){
					int dataUsed = usageMapForDevice.get(country);
					totalUsageInCountry += dataUsed;
				}
				else{
					usageMapForDevice.put(country, 0);
					usageRecord.setUsage(usageMapForDevice);
				}
			}
			totalUsageMapByCountry.put(country, totalUsageInCountry);
			totalUsageOverall += totalUsageInCountry;
			
		}
		
		totalUsageRecord.setUsage(totalUsageMapByCountry);
		totalUsageRecord.setTotal(totalUsageOverall);
		usageRecords.add(totalUsageRecord);
		
		usageRecordsByLocation.setUsageRecords(usageRecords);
		
		return usageRecordsByLocation;
	}

}
