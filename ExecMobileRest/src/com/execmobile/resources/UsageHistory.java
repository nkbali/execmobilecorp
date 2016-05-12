/**
 * 
 */
package com.execmobile.resources;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.SessionFactory;

import com.execmobile.data.DeviceHome;
import com.execmobile.data.UsageHome;
import com.execmobile.helpers.DBSessionManager;
import com.execmobile.helpers.ExcelParser;
import com.execmobile.helpers.ReportParser;
import com.execmobile.helpers.ResponseBuilderWithCacheControl;
import com.execmobile.helpers.UsageFetcher;
import com.execmobile.models.UsageByLocation;
import com.execmobile.models.UsageRecordsByLocation;
import com.execmobile.models.YearlyUsage;
import com.execmobile.models.YearlyUsageReport;

/**
 * @author Nakul's Dev Machine
 *
 */
@Path("/usageHistory")
@Consumes({ "application/json", "multipart/form-data" })
@Produces({ "application/json" })
public class UsageHistory {

	private static final Log log = LogFactory.getLog(UsageHistory.class);
	String zone = null;
	
	SessionFactory sessionFactory = new DBSessionManager().getSessionFactory();

	@GET
	@Path("{days}")
	@RolesAllowed({"App"})
	public Response getSpotUsage(@PathParam("days") int days, @Context Request request, @HeaderParam("IMEI") String imei, @HeaderParam("Product") String productID) {
		Response.ResponseBuilder rb = null;
		try {
			
			List<com.execmobile.models.Usage> usageRecords = new UsageFetcher().getUsageHistory(imei, productID, days, sessionFactory);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(usageRecords, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Path("{month}/{year}")
	@RolesAllowed({"Admin"})
	public Response getMonthlyUsageRecord(@PathParam("month") int month, @PathParam("year") int year, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			
			List usageRecords = new UsageHome(sessionFactory).getMonthlyUsage(month, year);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(usageRecords, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Path("report/{year}")
	@RolesAllowed({"Admin"})
	public Response getYearlyUsageRecord(@PathParam("year") int year, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			
			List usageRecords = new UsageHome(sessionFactory).getYearlyUsageReport(year);
			YearlyUsageReport yearlyUsageReport = new ReportParser().parseYearlyUsage(usageRecords, year);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(yearlyUsageReport, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Path("report/{companyID}/{year}")
	@RolesAllowed({"Admin", "CompanyUser"})
	public Response getYearlyUsageRecordForCompany(@PathParam("companyID") String companyID, @PathParam("year") int year, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			
			List usageRecords = new UsageHome(sessionFactory).getYearlyUsageReportForCompany(companyID, year);
			YearlyUsageReport yearlyUsageReport = new ReportParser().parseYearlyUsage(usageRecords, year);
			List<com.execmobile.data.Device> companyDevices = new DeviceHome(sessionFactory).getCompanyDevices(companyID);
			List<YearlyUsage> parsedUsageRecords = yearlyUsageReport.getUsageRecords();
			List<YearlyUsage> allDevicesUsageRecords = new ArrayList<>();
			String company = parsedUsageRecords.get(0).getCompany();
			
			for(com.execmobile.data.Device companyDevice : companyDevices){
				Optional<YearlyUsage> optionalDevice = parsedUsageRecords.stream().filter(
						usageRecord -> usageRecord.getDeviceID().equals(companyDevice.getDeviceId()))
						.findFirst();
				if(optionalDevice.isPresent()){
					allDevicesUsageRecords.add(optionalDevice.get());
					continue;
				}
				
				YearlyUsage newDeviceUsageRecord = new YearlyUsage();
				newDeviceUsageRecord.setDeviceID(companyDevice.getDeviceId());
				newDeviceUsageRecord.setCompany(company);
				newDeviceUsageRecord.setProduct(companyDevice.getProduct().getName());
				newDeviceUsageRecord.setUsage1(0);
				newDeviceUsageRecord.setUsage2(0);
				newDeviceUsageRecord.setUsage3(0);
				newDeviceUsageRecord.setUsage4(0);
				newDeviceUsageRecord.setUsage5(0);
				newDeviceUsageRecord.setUsage6(0);
				newDeviceUsageRecord.setUsage7(0);
				newDeviceUsageRecord.setUsage8(0);
				newDeviceUsageRecord.setUsage9(0);
				newDeviceUsageRecord.setUsage10(0);
				newDeviceUsageRecord.setUsage11(0);
				newDeviceUsageRecord.setUsage12(0);
				newDeviceUsageRecord.setTotal(0);
				allDevicesUsageRecords.add(newDeviceUsageRecord);
			}
			yearlyUsageReport.setUsageRecords(allDevicesUsageRecords);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(yearlyUsageReport, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Path("report/device/{deviceID}/{year}")
	@RolesAllowed({"Admin", "CompanyUser"})
	public Response getYearlyUsageRecordForDevice(@PathParam("deviceID") String deviceID, @PathParam("year") int year, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			
			List usageRecords = new UsageHome(sessionFactory).getYearlyUsageReportForDevice(deviceID, year);
			YearlyUsageReport yearlyUsageReport = new ReportParser().parseYearlyUsage(usageRecords, year);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(yearlyUsageReport, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Path("report/location/{year}")
	@RolesAllowed({"Admin"})
	public Response getYearlyUsageRecordLocation(@PathParam("year") int year, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			
			List usageRecords = new UsageHome(sessionFactory).getYearlyUsageReportByLocation(year);
			UsageRecordsByLocation usageRecordsByLocation = new ReportParser().parseLocationBasedUsage(usageRecords);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(usageRecordsByLocation, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Path("report/location/{companyID}/{year}")
	@RolesAllowed({"Admin", "CompanyUser"})
	public Response getYearlyUsageRecordForCompanyByLocation(@PathParam("companyID") String companyID, @PathParam("year") int year, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			
			List usageRecords = new UsageHome(sessionFactory).getYearlyUsageReportForCompanyByLocation(companyID, year);
			UsageRecordsByLocation usageRecordsByLocation = new ReportParser().parseLocationBasedUsage(usageRecords);
			List<com.execmobile.data.Device> companyDevices = new DeviceHome(sessionFactory).getCompanyDevices(companyID);
			List<UsageByLocation> parsedUsageRecords = usageRecordsByLocation.getUsageRecords();
			List<UsageByLocation> allDevicesUsageRecords = new ArrayList<>();
			String company = parsedUsageRecords.get(0).getCompany();
			TreeMap<String, Integer> locations = parsedUsageRecords.get(0).getUsage();
			
			for(com.execmobile.data.Device companyDevice : companyDevices){
				Optional<UsageByLocation> optionalDevice = parsedUsageRecords.stream().filter(
						usageRecord -> usageRecord.getDeviceId().equals(companyDevice.getDeviceId()))
						.findFirst();
				if(optionalDevice.isPresent()){
					allDevicesUsageRecords.add(optionalDevice.get());
					continue;
				}
				
				UsageByLocation newDeviceUsageRecord = new UsageByLocation();
				newDeviceUsageRecord.setCompany(company);
				newDeviceUsageRecord.setProduct(companyDevice.getProduct().getName());
				newDeviceUsageRecord.setDeviceId(companyDevice.getDeviceId());
				TreeMap<String, Integer> usageLocations = new TreeMap<>();
				for(String location : locations.keySet())
				{
					usageLocations.put(location, 0);
				}
				newDeviceUsageRecord.setUsage(usageLocations);
				newDeviceUsageRecord.setTotal(0);
				allDevicesUsageRecords.add(newDeviceUsageRecord);
			}
			usageRecordsByLocation.setUsageRecords(allDevicesUsageRecords);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(usageRecordsByLocation, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Path("report/location/device/{deviceID}/{year}")
	@RolesAllowed({"Admin", "CompanyUser"})
	public Response getYearlyUsageRecordForDeviceByLocation(@PathParam("deviceID") String deviceID, @PathParam("year") int year, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			
			List usageRecords = new UsageHome(sessionFactory).getYearlyUsageReportForDeviceByLocation(deviceID, year);
			UsageRecordsByLocation usageRecordsByLocation = new ReportParser().parseLocationBasedUsage(usageRecords);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(usageRecordsByLocation, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@POST
	@RolesAllowed({"Admin"})
	public Response addUsageData(com.execmobile.data.Usage newUsageRecord, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (newUsageRecord == null || StringUtils.isBlank(newUsageRecord.getDevice().getDeviceId()))
				rb = Response.serverError();

			newUsageRecord = new UsageHome(sessionFactory).save(newUsageRecord);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(newUsageRecord, request);

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@POST
	@Path("monthlyUsage")
	@RolesAllowed({"Admin"})
	public Response uploadUsageExcel(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition data , @Context Request request) {
		
		try {
			byte[] excelData = IOUtils.toByteArray(fileInputStream);
				new ExcelParser().parseUsageFile(excelData, sessionFactory);
				sessionFactory.close();
			return Response.ok().build();
			
		} catch (Exception ex) {
			log.error(ex.getStackTrace().toString());
			sessionFactory.close();
			return Response.serverError().build();
		}
		
	}
}
