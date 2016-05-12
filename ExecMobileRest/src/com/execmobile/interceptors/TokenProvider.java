package com.execmobile.interceptors;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
public class TokenProvider implements ContainerResponseFilter{

	@Context
	private ResourceInfo resourceInfo;
	
	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
//		return;
		try
		{
			Set<String> roleSet = null;
			Method method = resourceInfo.getResourceMethod();
			if (method.isAnnotationPresent(RolesAllowed.class)) {
				RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
				roleSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
			}
			
			if(roleSet.contains("App") && request.getHeaders().containsKey("Token"))
			{
				response.getHeaders().add("Authorization", request.getHeaders().getFirst("Token"));
			}
			
			return;
		}
		catch(Exception ex)
		{
			
		}
		
		
	}
	
}
