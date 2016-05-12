/**
 * 
 */
package com.execmobile.resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import com.execmobile.data.CountryHome;
import com.execmobile.data.DeviceHome;
import com.execmobile.data.Faq;
import com.execmobile.data.FaqHome;
import com.execmobile.data.ProductHome;
import com.execmobile.data.Productcoverage;
import com.execmobile.data.ProductcoverageHome;
import com.execmobile.data.Productpriceplan;
import com.execmobile.data.ProductpriceplanHome;
import com.execmobile.data.ProductsupportHome;
import com.execmobile.data.ZoneHome;
import com.execmobile.helpers.DBSessionManager;
import com.execmobile.helpers.ResponseBuilderWithCacheControl;
import com.execmobile.helpers.UsageFetcher;
import com.execmobile.models.ProductDetails;
import com.execmobile.models.WebbingCredentials;


/**
 * @author Nakul's Dev Machine
 *
 */
@Path("/product")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class Product {

	private static final Log log = LogFactory.getLog(Product.class);
	@Context ServletContext servletContext;

	SessionFactory sessionFactory = new DBSessionManager().getSessionFactory();
	
	@GET
	@RolesAllowed({"Admin"})
	public Response getProducts(@Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			List<com.execmobile.data.Product> products = new ProductHome(sessionFactory).getProducts();
			JAXBContext jaxbContext = JAXBContext.newInstance(WebbingCredentials.class);    
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
			WebbingCredentials credentialsFactory = (WebbingCredentials) jaxbUnmarshaller.unmarshal(servletContext.getResourceAsStream("/WEB-INF/credentials.xml"));
			for(com.execmobile.data.Product product : products)
			{
				if(product.getUsername().equals(credentialsFactory.getTelkom().getUsername()))
					product.setUseTelkomCredentials(true);
			}
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(products, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@GET
	@Path("{productID}")
	@RolesAllowed({"Admin"})
	public Response getProduct(@PathParam("productID") String productID, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			com.execmobile.data.Product product = new ProductHome(sessionFactory).findById(productID);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(product, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@GET
	@Path("faq")
	@RolesAllowed({"App"})
	public Response getProductFaqs(@Context Request request, @HeaderParam("Product") String productID) {
		Response.ResponseBuilder rb = null;
		try {
			
			List<com.execmobile.data.Faq> faqs = new FaqHome(sessionFactory).getProductFaq(productID);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(faqs, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@GET
	@Path("{productID}/faq")
	@RolesAllowed({"Admin"})
	public Response getProductFaqsList(@PathParam("productID") String productID, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			List<com.execmobile.data.Faq> faqs = new FaqHome(sessionFactory).getProductFaq(productID);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(faqs, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		
		return rb.build();
	}
	
	@GET
	@Path("/faq/{faqID}")
	@RolesAllowed({"Admin"})
	public Response getProductFaq(@PathParam("faqID") String faqID, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			com.execmobile.data.Faq faq = new FaqHome(sessionFactory).findById(faqID);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(faq, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@GET
	@Path("zone")
	@RolesAllowed({"Admin"})
	public Response getProductZones(@Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			String[] productIDs = new String[4];
			productIDs[0] = "dde4635f-30cb-4541-b256-7a362a60fabb";
			List<com.execmobile.data.Zone> zones = new ZoneHome(sessionFactory).getProductZone(productIDs[0]);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(zones, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@GET
	@Path("{productID}/zone")
	@RolesAllowed({"Admin"})
	public Response getProductZonesList(@PathParam("productID") String productID, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			List<com.execmobile.data.Zone> zones = new ZoneHome(sessionFactory).getProductZone(productID);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(zones, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@GET
	@Path("/zone/{zoneID}")
	@RolesAllowed({"Admin"})
	public Response getProductZone(@PathParam("zoneID") String zoneID, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			com.execmobile.data.Zone zone = new ZoneHome(sessionFactory).findById(zoneID);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(zone, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@GET
	@Path("/support")
	@RolesAllowed({"App"})
	public Response getProductSupport(@Context Request request, @HeaderParam("Product") String productID) {
		Response.ResponseBuilder rb = null;
		try {
			
			com.execmobile.data.Productsupport productSupport = new ProductsupportHome(sessionFactory)
					.getProductSupport(productID);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(productSupport, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@GET
	@Path("{productID}/support")
	@RolesAllowed({"Admin"})
	public Response getProductSupportInfo(@PathParam("productID") String productID, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			com.execmobile.data.Productsupport productSupport = new ProductsupportHome(sessionFactory)
					.getProductSupport(productID);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(productSupport, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@GET
	@Path("/details")
	@RolesAllowed({"App"})
	public Response getProductPricePlanAndCoverage(@Context Request request, @HeaderParam("Product") String productID, @HeaderParam("IMEI") String imei) {
		Response.ResponseBuilder rb = null;
		try {
			
			ProductDetails productDetails = new ProductDetails();
			com.execmobile.data.Product product = new ProductHome(sessionFactory).findById(productID);
			productDetails.setProductPricePlan(new ProductpriceplanHome(sessionFactory).getProductPricePlan(productID));
			List<com.execmobile.data.Productcoverage> productCoverage = new ProductcoverageHome(sessionFactory)
					.getProductCoverage(productID);
			if(product.getName().equalsIgnoreCase("Monthly"))
			{
				
				int totalUsageForMonth = new UsageFetcher().getCurrentMonthsUsage(imei, productID, sessionFactory);
				Productpriceplan pricePlan = productDetails.getProductPricePlan();
				String productDetailsString = pricePlan.getDetails();
				productDetailsString = productDetailsString.replace("$usage", "" + totalUsageForMonth);
				pricePlan.setDetails(productDetailsString);
				productDetails.setProductPricePlan(pricePlan);
			}
			
			if(product.getName().equalsIgnoreCase("Telkom"))
			{
				int bundlesUsedSoFar = new DeviceHome(sessionFactory).findByImei(imei).getBundlesUsed();
				Productpriceplan pricePlan = productDetails.getProductPricePlan();
				String productDetailsString = pricePlan.getDetails();
				productDetailsString = productDetailsString.replace("$usage", "" + bundlesUsedSoFar);
				pricePlan.setDetails(productDetailsString);
				productDetails.setProductPricePlan(pricePlan);
			}
			
			Iterator<com.execmobile.data.Productcoverage> coverageIterator = productCoverage.iterator();
			productDetails.setProductCoverage(new ArrayList<com.execmobile.data.Country>());
			while (coverageIterator.hasNext()) {
				com.execmobile.data.Productcoverage coverage = coverageIterator.next();
				productDetails.getProductCoverage().add(coverage.getCountry());
			}
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(productDetails, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@GET
	@Path("{productID}/details")
	@RolesAllowed({"Admin"})
	public Response getProductDetailsInfo(@PathParam("productID") String productID, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {
			ProductDetails productDetails = new ProductDetails();
			productDetails.setProductPricePlan(new ProductpriceplanHome(sessionFactory).getProductPricePlan(productID));
			List<com.execmobile.data.Productcoverage> productCoverage = new ProductcoverageHome(sessionFactory)
					.getProductCoverage(productID);

			Iterator<com.execmobile.data.Productcoverage> coverageIterator = productCoverage.iterator();
			productDetails.setProductCoverage(new ArrayList<com.execmobile.data.Country>());
			while (coverageIterator.hasNext()) {
				com.execmobile.data.Productcoverage coverage = coverageIterator.next();
				productDetails.getProductCoverage().add(coverage.getCountry());
			}
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(productDetails, request);
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@POST
	@RolesAllowed({"Admin"})
	public Response addProduct(com.execmobile.data.Product newProduct, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (newProduct == null)
				rb = Response.serverError();
			
			JAXBContext jaxbContext = JAXBContext.newInstance(WebbingCredentials.class);    
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
			WebbingCredentials credentialsFactory = (WebbingCredentials) jaxbUnmarshaller.unmarshal(servletContext.getResourceAsStream("/WEB-INF/credentials.xml"));
			if(newProduct.isUseTelkomCredentials())
			{
				newProduct.setUsername(credentialsFactory.getTelkom().getUsername());
				newProduct.setPassword(credentialsFactory.getTelkom().getPassword());
				newProduct.setKey(credentialsFactory.getTelkom().getKey());
			}
			
			else
			{
				newProduct.setUsername(credentialsFactory.getDefault().getUsername());
				newProduct.setPassword(credentialsFactory.getDefault().getPassword());
				newProduct.setKey(credentialsFactory.getDefault().getKey());
			}

			newProduct = new ProductHome(sessionFactory).save(newProduct);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(newProduct, request);

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@POST
	@Path("faq")
	@RolesAllowed({"Admin"})
	public Response addFaq(com.execmobile.data.Faq newFaq, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (newFaq == null)
				rb = Response.serverError();
			final int newFaqPriority = newFaq.getPriority();
			List<com.execmobile.data.Faq> productFaqs = new FaqHome(sessionFactory).getProductFaq(newFaq.getProduct().getProductId());
			Optional<com.execmobile.data.Faq> samePriorityFaq = productFaqs.stream().filter(existingFaq -> existingFaq.getPriority()
					== newFaqPriority).findFirst();
			if(!samePriorityFaq.isPresent())
			{
				newFaq = new FaqHome(sessionFactory).save(newFaq);

				return new ResponseBuilderWithCacheControl<>().buildResponse(newFaq, request);
			}
			for (Faq faq : productFaqs) {
				if (faq.getPriority() >= newFaq.getPriority()) {
					faq.setPriority(faq.getPriority() + 1);
					new FaqHome(sessionFactory).update(faq);
				}
			}

			newFaq = new FaqHome(sessionFactory).save(newFaq);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(newFaq, request);

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@POST
	@Path("zone")
	@RolesAllowed({"Admin"})
	public Response addZone(com.execmobile.data.Zone newZone, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (newZone == null || StringUtils.isBlank(newZone.getProduct().getProductId()))
				rb = Response.serverError();
			

			newZone = new ZoneHome(sessionFactory).save(newZone);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(newZone, request);

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		
		return rb.build();
	}

	@POST
	@Path("support")
	@RolesAllowed({"Admin"})
	public Response addSupport(com.execmobile.data.Productsupport newProductSupport, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (newProductSupport == null)
				rb = Response.serverError();

			newProductSupport = new ProductsupportHome(sessionFactory).save(newProductSupport);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(newProductSupport, request);

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@POST
	@Path("details")
	@RolesAllowed({"Admin"})
	public Response addProductPricePlanAndCoverage(ProductDetails newProductDetails, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (newProductDetails == null)
				rb = Response.serverError();

			newProductDetails
					.setProductPricePlan(new ProductpriceplanHome(sessionFactory).save(newProductDetails.getProductPricePlan()));

			com.execmobile.data.Product product = new ProductHome(sessionFactory)
					.findById(newProductDetails.getProductPricePlan().getProduct().getProductId());
			for (com.execmobile.data.Country country : newProductDetails.getProductCoverage()) {
				com.execmobile.data.Productcoverage productCoverage = new Productcoverage();
				productCoverage.setProduct(product);
				country = new CountryHome(sessionFactory).findById(country.getCountryId());
				productCoverage.setCountry(country);
				new ProductcoverageHome(sessionFactory).save(productCoverage);
			}
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(newProductDetails, request);

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@PUT
	@RolesAllowed({"Admin"})
	public Response updateProduct(com.execmobile.data.Product existingProduct, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (existingProduct == null || StringUtils.isBlank(existingProduct.getProductId()))
				rb = Response.serverError();

			JAXBContext jaxbContext = JAXBContext.newInstance(WebbingCredentials.class);    
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
			WebbingCredentials credentialsFactory = (WebbingCredentials) jaxbUnmarshaller.unmarshal(servletContext.getResourceAsStream("/WEB-INF/credentials.xml"));
			if(existingProduct.isUseTelkomCredentials())
			{
				existingProduct.setUsername(credentialsFactory.getTelkom().getUsername());
				existingProduct.setPassword(credentialsFactory.getTelkom().getPassword());
				existingProduct.setKey(credentialsFactory.getTelkom().getKey());
			}
			
			else
			{
				existingProduct.setUsername(credentialsFactory.getDefault().getUsername());
				existingProduct.setPassword(credentialsFactory.getDefault().getPassword());
				existingProduct.setKey(credentialsFactory.getDefault().getKey());
			}
			new ProductHome(sessionFactory).update(existingProduct);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(existingProduct, request);

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@PUT
	@Path("faq")
	@RolesAllowed({"Admin"})
	public Response updateFaq(com.execmobile.data.Faq existingFaq, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (existingFaq == null || StringUtils.isBlank(existingFaq.getFaqid()))
				rb = Response.serverError();

			com.execmobile.data.Faq currentFaq = new FaqHome(sessionFactory).findById(existingFaq.getFaqid());
			if (currentFaq.getPriority() > existingFaq.getPriority()) {
				List<com.execmobile.data.Faq> faqsToMove = new FaqHome(sessionFactory).getProductFaqsBetweenPriority(
						existingFaq.getProduct().getProductId(), existingFaq.getPriority(),
						currentFaq.getPriority() - 1);
				for (com.execmobile.data.Faq faqToMove : faqsToMove) {
					faqToMove.setPriority(faqToMove.getPriority() + 1);
					new FaqHome(sessionFactory).update(faqToMove);
				}
			}

			else if (currentFaq.getPriority() < existingFaq.getPriority()) {
				List<com.execmobile.data.Faq> faqsToMoveUp = new FaqHome(sessionFactory).getProductFaqsBetweenPriority(
						existingFaq.getProduct().getProductId(), currentFaq.getPriority() + 1,
						existingFaq.getPriority());
				for (com.execmobile.data.Faq faqToMoveUp : faqsToMoveUp) {
					faqToMoveUp.setPriority(faqToMoveUp.getPriority() - 1);
					new FaqHome(sessionFactory).update(faqToMoveUp);
				}

			}

			new FaqHome(sessionFactory).update(existingFaq);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(existingFaq, request);

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@PUT
	@Path("zone")
	@RolesAllowed({"Admin"})
	public Response updateZone(com.execmobile.data.Zone existingZone, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (existingZone == null || StringUtils.isBlank(existingZone.getZoneid()))
				rb = Response.serverError();

			

			new ZoneHome(sessionFactory).update(existingZone);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(existingZone, request);

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@PUT
	@Path("details")
	@RolesAllowed({"Admin"})
	public Response updateProductPricePlanAndCoverage(ProductDetails existingProductDetails, @Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (existingProductDetails == null)
				rb = Response.serverError();

			new ProductpriceplanHome(sessionFactory).update(existingProductDetails.getProductPricePlan());
			com.execmobile.data.Product product = new ProductHome(sessionFactory)
					.findById(existingProductDetails.getProductPricePlan().getProduct().getProductId());

			List<com.execmobile.data.Productcoverage> currentCoverage = new ProductcoverageHome(sessionFactory)
					.getProductCoverage(product.getProductId());
			for (com.execmobile.data.Productcoverage currentCoverageRecord : currentCoverage) {
				new ProductcoverageHome(sessionFactory).delete(currentCoverageRecord);
			}

			for (com.execmobile.data.Country country : existingProductDetails.getProductCoverage()) {
				com.execmobile.data.Productcoverage newCoverage = new com.execmobile.data.Productcoverage();
				country = new CountryHome(sessionFactory).findById(country.getCountryId());
				newCoverage.setCountry(country);
				newCoverage.setProduct(product);
				new ProductcoverageHome(sessionFactory).save(newCoverage);
			}
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(existingProductDetails, request);

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@PUT
	@Path("support")
	@RolesAllowed({"Admin"})
	public Response updateProductSupportInfo(com.execmobile.data.Productsupport existingProductSupport,
			@Context Request request) {
		Response.ResponseBuilder rb = null;
		try {

			if (existingProductSupport == null || StringUtils.isBlank(existingProductSupport.getProductSupportId()))
				rb = Response.serverError();

			new ProductsupportHome(sessionFactory).update(existingProductSupport);
			sessionFactory.close();
			return new ResponseBuilderWithCacheControl<>().buildResponse(existingProductSupport, request);

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@DELETE
	@Path("faq/{faqID}")
	@RolesAllowed({"Admin"})
	public Response deleteFaq(@PathParam("faqID") String faqID) {
		Response.ResponseBuilder rb = null;
		try {

			if (StringUtils.isBlank(faqID))
				rb = Response.serverError();

			com.execmobile.data.Faq faqToDelete = new FaqHome(sessionFactory).findById(faqID);

			List<com.execmobile.data.Faq> faqsToMoveUp = new FaqHome(sessionFactory)
					.getProductFaqsBelowPriority(faqToDelete.getProduct().getProductId(), faqToDelete.getPriority());
			for (com.execmobile.data.Faq faqToMoveUp : faqsToMoveUp) {
				faqToMoveUp.setPriority(faqToMoveUp.getPriority() - 1);
				new FaqHome(sessionFactory).update(faqToMoveUp);
			}

			new FaqHome(sessionFactory).delete(faqToDelete);
			sessionFactory.close();
			rb = Response.ok();
			return rb.build();

		} catch (Exception ex) {
			log.error("Could not add company", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}

	@HEAD
	@Path("activate/{productID}")
	@RolesAllowed({"Admin"})
	public Response activateProduct(@PathParam("productID") String productID)
	{
		Response.ResponseBuilder rb = null;
		try {
			com.execmobile.data.Product product = new ProductHome(sessionFactory).findById(productID);
			product.setIsActive(true);
			new ProductHome(sessionFactory).update(product);
			rb = Response.ok();
			sessionFactory.close();
			return rb.build();
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
	
	@HEAD
	@Path("deactivate/{productID}")
	@RolesAllowed({"Admin"})
	public Response deactivateProduct(@PathParam("productID") String productID)
	{
		Response.ResponseBuilder rb = null;
		try {
			com.execmobile.data.Product product = new ProductHome(sessionFactory).findById(productID);
			product.setIsActive(false);
			new ProductHome(sessionFactory).update(product);
			rb = Response.ok();
			sessionFactory.close();
			return rb.build();
		} catch (Exception ex) {
			log.error("Could not fetch companies", ex);
			sessionFactory.close();
			rb = Response.serverError();
		}
		

		return rb.build();
	}
}
