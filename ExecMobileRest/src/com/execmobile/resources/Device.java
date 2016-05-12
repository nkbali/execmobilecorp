/**
 * 
 */
package com.execmobile.resources;



import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

/**
 * @author Nakul's Dev Machine
 *
 */
@Path("/device")
@Produces({"application/json"})
@Consumes({"application/json"})
public class Device {
	
	private static final Log log = LogFactory.getLog(Device.class);
	
	SessionFactory sessionFactory = new DBSessionManager().getSessionFactory();
	
	@GET
	@Path("{id}")
	@RolesAllowed({"Admin"})
	public Response getDevice(@PathParam("id") String id, @Context Request request)
	{
		Response.ResponseBuilder rb = null;
		try {
			com.execmobile.data.Device device = new DeviceHome(sessionFactory).findById(id);
			if (device == null){
				sessionFactory.close();
				rb = Response.ok();
			}

			else {
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(device, request);
			}

		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	
}
