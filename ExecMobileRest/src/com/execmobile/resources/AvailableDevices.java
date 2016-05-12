package com.execmobile.resources;

import java.io.InputStream;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.SessionFactory;

import com.execmobile.data.DeviceHome;
import com.execmobile.helpers.DBSessionManager;
import com.execmobile.helpers.ExcelParser;
import com.execmobile.helpers.ResponseBuilderWithCacheControl;

@Path("/available")
@Produces({"application/json"})
@Consumes({"application/json", "multipart/form-data"})
public class AvailableDevices {
	
private static final Log log = LogFactory.getLog(AvailableDevices.class);
	
	SessionFactory sessionFactory = new DBSessionManager().getSessionFactory();
	
	@GET
	@RolesAllowed({"Admin"})
	public Response getAvailableDevices(@Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {
			List<com.execmobile.data.Device> devices = new DeviceHome(sessionFactory).getAvailableDevices();
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
	
	@PUT
	@RolesAllowed({"Admin"})
	public Response linkDevices(List<com.execmobile.data.Device> existingDevices, @Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {
				new DeviceHome(sessionFactory).updateDevices(existingDevices);
				sessionFactory.close();
				rb = Response.ok();


		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@POST
	@RolesAllowed({"Admin"})
	public Response addNewDevicesSheet(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition data , @Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {
			
			byte[] excelData = IOUtils.toByteArray(fileInputStream);
			new ExcelParser().parseDevicesFile(excelData, sessionFactory);
			sessionFactory.close();
			rb = Response.ok();

		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

}
