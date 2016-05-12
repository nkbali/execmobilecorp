package com.execmobile.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import com.execmobile.data.SessionlistHome;
import com.execmobile.helpers.DBSessionManager;

@Path("/logout")
@Produces({ "application/json" })
@Consumes({ "application/json" })
public class Logout {

	private static final Log log = LogFactory.getLog(Logout.class);

	SessionFactory sessionFactory = new DBSessionManager().getSessionFactory();

	@GET
	@RolesAllowed({ "Admin", "CompanyUser" })
	public void logout(@Context Request request, @Context HttpHeaders headers) {
		try {
			String accessToken = headers.getHeaderString("Authorization");
			com.execmobile.data.Sessionlist currentSession = new SessionlistHome(sessionFactory)
					.findByToken(accessToken);
			if (currentSession != null) {
				if (!StringUtils.isBlank(currentSession.getCompany().getCompanyId())) {
					new SessionlistHome().removeSession(currentSession);
				}
			}
			sessionFactory.close();
		} catch (Exception ex) {
			log.error(ex.getStackTrace().toString());
		}
	}

}
