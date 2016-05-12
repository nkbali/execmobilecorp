package com.execmobile.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import com.execmobile.data.ApploginhistoryHome;
import com.execmobile.helpers.DBSessionManager;
import com.execmobile.helpers.Mailer;
import com.execmobile.helpers.ResponseBuilderWithCacheControl;
import com.execmobile.models.MailMessage;

import javax.ws.rs.core.Context;

@Path("/appUsers")
@Produces({"application/json"})
@Consumes({"application/json"})
public class AppUsers {
	
private static final Log log = LogFactory.getLog(AppUsers.class);
	
	SessionFactory sessionFactory = new DBSessionManager().getSessionFactory();
	
	
	@GET
	@RolesAllowed({"Admin"})
	public Response getAppUsers(@Context Request request){
		Response.ResponseBuilder rb = null;
		try {
			List<com.execmobile.data.Apploginhistory> appLoginHistory = new ApploginhistoryHome(sessionFactory).findAppUsers();
			if (appLoginHistory.isEmpty()){
				sessionFactory.close();
				rb = Response.ok();
			}

			else {
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(appLoginHistory, request);
			}

		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		return rb.build();
	}

	@POST
	@RolesAllowed({"Admin"})
	public Response sendMailToAppUsers(MailMessage mailMessage, @Context Request request){
		Response.ResponseBuilder rb = null;
		try {
			if (mailMessage.getReceivers().isEmpty()){
				sessionFactory.close();
				rb = Response.ok();
			}

			else {
				sessionFactory.close();
				new Mailer().sendMailToAppUsers(mailMessage.getReceivers(), mailMessage.getSubject(), mailMessage.getBody());
				rb = Response.ok();
			}

		} catch (Exception ex) {
			log.error("Failed to send email", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		return rb.build();
	}
}
