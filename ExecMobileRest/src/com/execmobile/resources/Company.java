/**
 * 
 */
package com.execmobile.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import com.execmobile.data.CompanyHome;
import com.execmobile.data.DeviceHome;
import com.execmobile.helpers.DBSessionManager;
import com.execmobile.helpers.ResponseBuilderWithCacheControl;

/**
 * @author Nakul's Dev Machine
 *
 */
@Path("/company")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class Company {

	private static final Log log = LogFactory.getLog(Company.class);
	
	SessionFactory sessionFactory = new DBSessionManager().getSessionFactory();

	@GET
	@RolesAllowed({"Admin"})
	public Response getCompanies(@Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			List<com.execmobile.data.Company> companies = new CompanyHome(sessionFactory).getCompanies();
			if (companies.isEmpty()){
				sessionFactory.close();
				rb = Response.ok();
			}

			else {
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(companies, request);
			}

		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@GET
	@Path("{companyId}/device")
	@RolesAllowed({"Admin", "CompanyUser"})
	public Response getCompanyDevices(@PathParam("companyId") String companyId, @Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {
			List<com.execmobile.data.Device> devices = new DeviceHome(sessionFactory).getCompanyDevices(companyId);
			if (devices.isEmpty()){
				sessionFactory.close();
				rb = Response.ok();
			}

			else {
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(devices, request);
			}

		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@GET
	@Path("{id}")
	@RolesAllowed({"Admin"})
	public Response getCompanyByID(@PathParam("id") String id, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			com.execmobile.data.Company company = new CompanyHome(sessionFactory).findById(id);
			if (company == null){
				sessionFactory.close();
				rb = Response.ok();
			}
			else{
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(company, request);
			}

		} catch (Exception ex) {
			log.error("Could not fetch company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@POST
	@RolesAllowed({"Admin"})
	public Response addCompany(com.execmobile.data.Company newCompany, @Context Request request) {

		Response.ResponseBuilder rb = null;
		try {

			if (newCompany == null){
				sessionFactory.close();
				rb = Response.serverError();
			}
			else{
				newCompany = new CompanyHome(sessionFactory).save(newCompany);
				sessionFactory.close();
				if (newCompany == null || StringUtils.isBlank(newCompany.getCompanyId()))
					rb = Response.serverError();
				else
					return new ResponseBuilderWithCacheControl<>().buildResponse(newCompany, request);
			}
		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();

	}

	@POST
	@Path("device")
	@RolesAllowed({"Admin"})
	public Response addCompanyDevice(com.execmobile.data.Device newCompanyDevice, @Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {

			if (newCompanyDevice == null || StringUtils.isBlank(newCompanyDevice.getCompany().getCompanyId()) || StringUtils.isBlank(newCompanyDevice.getDeviceId())){
				sessionFactory.close();
				rb = Response.serverError();
			}

			else{
				new DeviceHome(sessionFactory).update(newCompanyDevice);
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(newCompanyDevice, request);
			}

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@PUT
	@RolesAllowed({"Admin"})
	public Response modifyCompany(com.execmobile.data.Company newCompany, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (newCompany == null || StringUtils.isBlank(newCompany.getCompanyId())){
				sessionFactory.close();
				rb = Response.serverError();
			}
			
			else{
				new CompanyHome(sessionFactory).update(newCompany);
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(newCompany, request);
			}
		} catch (Exception ex) {
			log.error("Could not update company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@PUT
	@Path("device")
	@RolesAllowed({"Admin"})
	public Response updateCompanyDevice(com.execmobile.data.Device existingCompanyDevice, @Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {

			if (existingCompanyDevice == null || StringUtils.isBlank(existingCompanyDevice.getCompany().getCompanyId()) || StringUtils.isBlank(existingCompanyDevice.getDeviceId())){
				sessionFactory.close();
				rb = Response.serverError();
			}
			else{
				new DeviceHome(sessionFactory).update(existingCompanyDevice);
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(existingCompanyDevice, request);
			}

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@HEAD
	@Path("device/{deviceId}/activate")
	@RolesAllowed({"Admin"})
	public Response activateCompanyDevice(@PathParam("deviceId") String deviceId, @Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {
			com.execmobile.data.Device device = new DeviceHome(sessionFactory).findById(deviceId);
			if (device == null){
				sessionFactory.close();
				rb = Response.status(Status.NOT_FOUND);
			}

			else {
				device.setIsActive(true);
				new DeviceHome(sessionFactory).update(device);
				sessionFactory.close();
				rb = Response.ok();
				return rb.build();
			}

		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@HEAD
	@Path("device/{deviceId}/deactivate")
	@RolesAllowed({"Admin"})
	public Response deactivateCompanyDevice(@PathParam("deviceId") String deviceId, @Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {
			com.execmobile.data.Device device = new DeviceHome(sessionFactory).findById(deviceId);
			if (device == null){
				sessionFactory.close();
				rb = Response.status(Status.NOT_FOUND);
			}

			else {
				device.setIsActive(false);
				new DeviceHome(sessionFactory).update(device);
				sessionFactory.close();
				rb = Response.ok();
				return rb.build();
			}

		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
}
