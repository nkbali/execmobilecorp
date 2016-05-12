/**
 * 
 */
package com.execmobile.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import com.execmobile.data.CountryHome;
import com.execmobile.helpers.DBSessionManager;
import com.execmobile.helpers.ResponseBuilderWithCacheControl;

/**
 * @author Nakul's Dev Machine
 *
 */
@Path("/country")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class Country {

	private static final Log log = LogFactory.getLog(Company.class);

	SessionFactory sessionFactory = new DBSessionManager().getSessionFactory();
	
	@GET
	@RolesAllowed({"Admin"})
	public Response getCountries(@Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			List<com.execmobile.data.Country> countries = new CountryHome(sessionFactory).getCountries();
			if (countries.isEmpty()){
				sessionFactory.close();
				rb = Response.ok();
			}

			else {
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(countries, request);
			}

		} catch (Exception ex) {
			log.error("Could not fetch countries", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@GET
	@Path("{id}")
	@RolesAllowed({"Admin"})
	public Response getCountryByID(@PathParam("id") String id, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			com.execmobile.data.Country country = new CountryHome(sessionFactory).findById(id);
			if (country == null){
				sessionFactory.close();
				rb = Response.ok();
			}
			else{
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(country, request);
			}

		} catch (Exception ex) {
			log.error("Could not fetch country", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@POST
	@RolesAllowed({"Admin"})
	public Response addCountry(com.execmobile.data.Country newCountry, @Context Request request) {

		Response.ResponseBuilder rb = null;
		try {

			if (newCountry == null){
				sessionFactory.close();
				rb = Response.serverError();
			}

			newCountry = new CountryHome(sessionFactory).save(newCountry);
			if (newCountry == null || StringUtils.isBlank(newCountry.getCountryId())){
				sessionFactory.close();
				rb = Response.serverError();
			}
			else{
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(newCountry, request);
			}

		} catch (Exception ex) {
			log.error("Could not add country", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();

	}

	@PUT
	@RolesAllowed({"Admin"})
	public Response modifyCountry(com.execmobile.data.Country newCountry, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (newCountry == null || StringUtils.isBlank(newCountry.getCountryId())){
				sessionFactory.close();
				rb = Response.serverError();
			}
			else{
				new CountryHome(sessionFactory).update(newCountry);
				sessionFactory.close();
				return new ResponseBuilderWithCacheControl<>().buildResponse(newCountry, request);
			}

		} catch (Exception ex) {
			log.error("Could not update country", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		
		return rb.build();
	}

}
