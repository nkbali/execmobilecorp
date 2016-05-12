/**
 * 
 */
package com.execmobile.helpers;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author nakulbali
 * @param <T>
 *
 */
public class ResponseBuilderWithCacheControl<T> {
	
	
	public Response buildResponse(T responseData, Request request)
	{
		Response.ResponseBuilder rb = null;
		CacheControl cc = new CacheControl();
		//Set max age to one day
		cc.setMaxAge(86400);

		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(responseData);
		

		//Calculate the ETag on last modified date of user resource  
		EntityTag etag = new EntityTag(hashCodeBuilder.toHashCode()+"");

		//Verify if it matched with etag available in http request
		rb = request.evaluatePreconditions(etag);

		//If ETag matches the rb will be non-null; 
		//Use the rb to return the response without any further processing
		if (rb != null) 
		{
			return rb.cacheControl(cc).tag(etag).build();
		}

		//If rb is null then either it is first time request; or resource is modified
		//Get the updated representation and return with Etag attached to it
		rb = Response.ok(responseData).cacheControl(cc).tag(etag);
		return rb.build();
	}

}
