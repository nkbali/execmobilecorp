package com.execmobile.resources;


import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import com.execmobile.data.DeviceHome;
import com.execmobile.helpers.DBSessionManager;
import com.execmobile.helpers.ResponseBuilderWithCacheControl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Path("/allocated")
@Api(value="/allocated")
@Produces({"application/json"})
@Consumes({"application/json", "multipart/form-data"})
public class AllocatedDevices {
	
private static final Log log = LogFactory.getLog(AllocatedDevices.class);
	
	SessionFactory sessionFactory = new DBSessionManager().getSessionFactory();
	
	@GET
	@RolesAllowed({"Admin"})
	@ApiOperation(value = "Get list of all allocated devices",
    notes = "List of allocated devices",
    response = com.execmobile.data.Device.class,
    responseContainer = "List",
    authorizations = {
            @Authorization(
                    value="admin", 
                    scopes = {
                            @AuthorizationScope(
                                    scope = "Admin", 
                                    description = "System admin user can access list of all allocated devices")
                            }
                    )
      })
	public Response getAllocatedDevices(@Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {
			List<com.execmobile.data.Device> devices = new DeviceHome(sessionFactory).getAllocatedDevices();
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
	@Path("{searchText}")
	@RolesAllowed({"Admin"})
	public Response searchDevices(@PathParam("searchText") String searchText, @Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {
			List<com.execmobile.data.Device> devices = new DeviceHome(sessionFactory).searchDevices(searchText);
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
	public Response moodifyDevice(com.execmobile.data.Device existingDevice, @Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {
				new DeviceHome(sessionFactory).update(existingDevice);
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

