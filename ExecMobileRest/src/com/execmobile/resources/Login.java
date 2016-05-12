package com.execmobile.resources;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.execmobile.data.CompanyHome;
import com.execmobile.data.Sessionlist;
import com.execmobile.data.SessionlistHome;
import com.execmobile.helpers.DBSessionManager;
import com.execmobile.helpers.DataSecurity;
import com.execmobile.helpers.Mailer;
import com.execmobile.helpers.ResponseBuilderWithCacheControl;
import com.execmobile.models.LoginDetails;
import com.execmobile.models.User;
import com.execmobile.models.UserCredentials;

@Path("/login")
@Produces({"application/json"})
@Consumes({"application/json"})
public class Login {
	
	private static final Log log = LogFactory.getLog(Login.class);
	
	SessionFactory sessionFactory = new DBSessionManager().getSessionFactory();

	
	@POST
	public Response login(LoginDetails loginDetails, @HeaderParam("RequestID") String requestID, @Context Request request)
	{
		Response.ResponseBuilder rb = null;
		UserCredentials userCredentials = new UserCredentials();
		User user = new User();
		try
		{
			com.execmobile.data.Company userCompany = new CompanyHome(sessionFactory).findByUsername(loginDetails.getUsername());
			if(userCompany == null)
			{
				rb = Response.status(Status.UNAUTHORIZED);
				return rb.build();
			}
			String decryptedUsername = new DataSecurity(userCompany.getPassword(), 128).decryptData(loginDetails.getLoginToken());
			if(userCompany.getUsername().trim().equalsIgnoreCase(decryptedUsername.trim()))
			{
				String lastAccessTime = DateTime.now(DateTimeZone.UTC).toString();
				String accessTokenBase = loginDetails.getUsername() + ":" + userCompany.getPassword() + ":" + lastAccessTime + ":" + requestID;
				List<com.execmobile.data.Sessionlist> sessions = new SessionlistHome(sessionFactory).getCompanySessions(userCompany.getCompanyId());
				int sessionCount = 0;
				for(com.execmobile.data.Sessionlist session : sessions){
					DateTime sessionTime = DateTime.parse(session.getLastAccessTime());
					long diffInMillis = DateTime.now(DateTimeZone.UTC).getMillis() - sessionTime.getMillis();
					int seconds = (int) ((diffInMillis / 1000) % 60);
					long minutes = ((diffInMillis - seconds) / 1000) / 60;
					if(minutes >= 30)
					{
						new SessionlistHome(sessionFactory).removeSession(session);
						continue;
					}
					sessionCount++;
				}
				if(sessionCount >= 3){
					rb = Response.status(Status.FORBIDDEN);
					return rb.build();
				}
				
				String hash = "";
				try {
					hash = DatatypeConverter
							.printHexBinary(MessageDigest.getInstance("MD5").digest(accessTokenBase.getBytes("UTF-8")));
				} catch (NoSuchAlgorithmException ex) {
					rb = Response.serverError();
					return rb.build();
				}
				com.execmobile.data.Sessionlist newSession = new Sessionlist();
				newSession.setSessionId(requestID);
				newSession.setAccessToken(hash);
				newSession.setCompany(userCompany);
				newSession.setLastAccessTime(lastAccessTime);
				
				user.setAccessToken(hash);
				if(userCompany.getName().equalsIgnoreCase("ExecMobile")){
					user.setRole("Admin");
				}
				else
				{
					user.setRole("CompanyUser");
					userCredentials.setCompany(userCompany);
				}
				new SessionlistHome(sessionFactory).save(newSession);
				userCredentials.setUser(user);
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(userCredentials, request);
			}
			else
			{
				rb = Response.status(Status.UNAUTHORIZED);
			}
		}
		catch(Exception ex)
		{
			log.error(ex.getStackTrace().toString());
			rb = Response.serverError();
		}
		
		return rb.build();
	}
	
	@GET
	@Path("/requestPasswordReset/{username}")
	public Response requestPasswordReset(@PathParam("username") String username)
	{
		com.execmobile.data.Company userCompany = new CompanyHome(sessionFactory).findByUsername(username);
		if(userCompany != null)
		{
			new Mailer().sendPasswordResetMail(userCompany.getName());
		}
		sessionFactory.close();
		return Response.ok().build();
	}

}
