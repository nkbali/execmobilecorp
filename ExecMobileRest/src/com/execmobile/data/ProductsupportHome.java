package com.execmobile.data;
// Generated Jan 31, 2016 10:08:30 PM by Hibernate Tools 4.3.1.Final


import java.util.List;
import java.util.UUID;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


/**
 * Home object for domain model class Productsupport.
 * @see com.execmobile.data.Productsupport
 * @author Hibernate Tools
 */
public class ProductsupportHome {

	private static final Log log = LogFactory.getLog(ProductsupportHome.class);

	private final SessionFactory sessionFactory;
	
	public ProductsupportHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Productsupport save(Productsupport transientInstance) {
		log.debug("persisting Company instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			transientInstance.setProductSupportId(UUID.randomUUID().toString());
			dbSession.save(transientInstance);
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			log.debug("save successful");
			return transientInstance;
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
		
	}

	public void update(Productsupport instance) {
		log.debug("attaching dirty Product instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			dbSession.saveOrUpdate(instance);
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Productsupport persistentInstance) {
		log.debug("deleting Productsupport instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Productsupport merge(Productsupport detachedInstance) {
		log.debug("merging Productsupport instance");
		try {
			Productsupport result = (Productsupport) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Productsupport findById(java.lang.String id) {
		log.debug("getting Productsupport instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Productsupport instance = (Productsupport) dbSession
					.get("com.execmobile.data.Productsupport", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public Productsupport getProductSupport(String productID) {
		log.debug("finding Productpriceplan instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			dbSession.beginTransaction();
			List<Productsupport> productSupports = (List<Productsupport>)dbSession.createCriteria("com.execmobile.data.Productsupport")
					.add(Restrictions.eq("product.productId", productID)).list();
			if(productSupports == null)
				return null;
			if(productSupports.isEmpty())
				return null;
			Productsupport productSupport = productSupports.get(0);
			dbSession.close();
			return productSupport;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
