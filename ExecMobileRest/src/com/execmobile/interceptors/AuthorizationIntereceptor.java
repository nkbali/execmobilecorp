/**
 * 
 */
package com.execmobile.interceptors;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Method;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.execmobile.data.Apploginhistory;
import com.execmobile.data.ApploginhistoryHome;
import com.execmobile.data.CompanyHome;
import com.execmobile.data.DeviceHome;
import com.execmobile.data.SessionlistHome;

/**
 * @author Nakul's Dev Machine
 *
 */
@Provider
public class AuthorizationIntereceptor implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container
	 * .ContainerRequestContext)
	 */
	@Override
	public void filter(ContainerRequestContext request) throws IOException, WebApplicationException {
		// return;
		if(request.getUriInfo().getPath().equalsIgnoreCase("swagger.json"))
			return;
		Set<String> roleSet = null;
		DeviceHome deviceHome = new DeviceHome();
		CompanyHome companyHome = new CompanyHome();
		SessionlistHome sessionHome = new SessionlistHome();
		ApploginhistoryHome appLoginHistoryHome = new ApploginhistoryHome();
		Method method = resourceInfo.getResourceMethod();
		if (method.isAnnotationPresent(RolesAllowed.class)) {
			RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
			roleSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
		}
		else if(method.getName().equals("login") || method.getName().equals("requestPasswordReset"))
		{
			if(method.getName().equals("login")){
				if(request.getHeaders().containsKey("RequestID")){
					deviceHome.closeSession();
					companyHome.closeSession();
					appLoginHistoryHome.closeSession();
					sessionHome.closeSession();
					return;
				}
				else
				{
					deviceHome.closeSession();
					companyHome.closeSession();
					appLoginHistoryHome.closeSession();
					sessionHome.closeSession();
					throw new WebApplicationException(Response.Status.UNAUTHORIZED);
				}
			}
			deviceHome.closeSession();
			companyHome.closeSession();
			appLoginHistoryHome.closeSession();
			sessionHome.closeSession();
			return;
		}
		if (roleSet.contains("App")) {
			if (request.getHeaders().containsKey("DeviceID") && request.getHeaders().containsKey("Email")) {
				com.execmobile.data.Device device = deviceHome
						.findById(request.getHeaders().getFirst("DeviceID"));
				if (device == null) {
					String deviceID = request.getHeaders().getFirst("DeviceID");
					deviceID = deviceID.replace("Webbing", "Webbing4G-");
					com.execmobile.data.Device exceptionalDevice = deviceHome
							.findById(deviceID);
					if(exceptionalDevice == null){
						deviceHome.closeSession();
						companyHome.closeSession();
						appLoginHistoryHome.closeSession();
						sessionHome.closeSession();
						throw new WebApplicationException(Response.Status.UNAUTHORIZED);
					}
					else
						device = exceptionalDevice;
				}
				com.execmobile.data.Product product = device.getProduct();
				com.execmobile.data.Apploginhistory appLoginHistory = new Apploginhistory();
				appLoginHistory.setEmail(request.getHeaders().getFirst("Email"));
				appLoginHistory.setDevice(device);
				List<Apploginhistory> sameEmailRecords = appLoginHistoryHome.findByEmail(appLoginHistory);
				if(!sameEmailRecords.isEmpty())
				{
					for(Apploginhistory sameEmailRecord : sameEmailRecords){
						appLoginHistoryHome.delete(sameEmailRecord);
					}
				}
				String tokenBase = device.getDeviceId() + ":" + request.getHeaders().getFirst("Email") + ":"
						+ DateTime.now(DateTimeZone.UTC).toString();
				String hash = "";
				try {
					hash = DatatypeConverter
							.printHexBinary(MessageDigest.getInstance("MD5").digest(tokenBase.getBytes("UTF-8")));
				} catch (NoSuchAlgorithmException ex) {
					deviceHome.closeSession();
					companyHome.closeSession();
					appLoginHistoryHome.closeSession();
					sessionHome.closeSession();
					throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
				}
				appLoginHistory.setToken(hash);
				appLoginHistory.setLastAccessTime(DateTime.now(DateTimeZone.UTC).toString());
				appLoginHistory = appLoginHistoryHome.save(appLoginHistory);
				request.getHeaders().add("Token", appLoginHistory.getToken());
				request.getHeaders().add("IMEI", device.getImei());
				request.getHeaders().add("Product", product.getProductId());
				deviceHome.closeSession();
				companyHome.closeSession();
				appLoginHistoryHome.closeSession();
				sessionHome.closeSession();
				return;
			}

			else if (request.getHeaders().containsKey("Authorization")) {
				String token = request.getHeaders().getFirst("Authorization");
				Apploginhistory appLoginRecord = appLoginHistoryHome.findByToken(token);
				if (appLoginRecord == null){
					deviceHome.closeSession();
					companyHome.closeSession();
					appLoginHistoryHome.closeSession();
					sessionHome.closeSession();
					throw new WebApplicationException(Response.Status.UNAUTHORIZED);
				}
				DateTime lastAccessTime = DateTime.parse(appLoginRecord.getLastAccessTime());
				DateTime currentTime = DateTime.now(DateTimeZone.UTC);
				long diffInMillis = currentTime.getMillis() - lastAccessTime.getMillis();
				int seconds = (int) ((diffInMillis / 1000) % 60);
				long minutes = ((diffInMillis - seconds) / 1000) / 60;

				if (minutes >= 30) {
					deviceHome.closeSession();
					companyHome.closeSession();
					appLoginHistoryHome.closeSession();
					sessionHome.closeSession();
					throw new WebApplicationException(Response.Status.UNAUTHORIZED);
				}
				appLoginRecord.setLastAccessTime(currentTime.toString());
				com.execmobile.data.Device device = deviceHome
						.findById(appLoginRecord.getDevice().getDeviceId());
				appLoginHistoryHome.update(appLoginRecord);
				com.execmobile.data.Product product = device.getProduct();
				request.getHeaders().add("Product", product.getProductId());
				request.getHeaders().add("IMEI", device.getImei());
				deviceHome.closeSession();
				companyHome.closeSession();
				sessionHome.closeSession();
				appLoginHistoryHome.closeSession();
				return;
			} else {
				deviceHome.closeSession();
				companyHome.closeSession();
				appLoginHistoryHome.closeSession();
				sessionHome.closeSession();
				throw new WebApplicationException(Response.Status.UNAUTHORIZED);
			}
		}
		else if(roleSet.contains("Admin") || roleSet.contains("CompanyUser"))
		{
			String accessToken = request.getHeaders().getFirst("Authorization");
			com.execmobile.data.Sessionlist currentSession = sessionHome.findByToken(accessToken);
			if(currentSession == null)
			{
				deviceHome.closeSession();
				companyHome.closeSession();
				appLoginHistoryHome.closeSession();
				sessionHome.closeSession();
				throw new WebApplicationException(Response.Status.UNAUTHORIZED);
			}
			else
			{
				DateTime lastAccessTime = DateTime.parse(currentSession.getLastAccessTime());
				DateTime currentTime = DateTime.now(DateTimeZone.UTC);
				long diffInMillis = currentTime.getMillis() - lastAccessTime.getMillis();
				int seconds = (int) ((diffInMillis / 1000) % 60);
				long minutes = ((diffInMillis - seconds) / 1000) / 60;

				if (minutes >= 30) {
					sessionHome.removeSession(currentSession);
					deviceHome.closeSession();
					companyHome.closeSession();
					appLoginHistoryHome.closeSession();
					sessionHome.closeSession();
					throw new WebApplicationException(Response.Status.UNAUTHORIZED);
				}
				currentSession.setLastAccessTime(currentTime.toString());
				sessionHome.update(currentSession);
				deviceHome.closeSession();
				companyHome.closeSession();
				appLoginHistoryHome.closeSession();
				sessionHome.closeSession();
				return;
			}
		}
		deviceHome.closeSession();
		companyHome.closeSession();
		appLoginHistoryHome.closeSession();
		sessionHome.closeSession();
		throw new WebApplicationException(Response.Status.UNAUTHORIZED);

	}

}
