package com.execmobile.data;
// Generated Jan 31, 2016 10:08:30 PM by Hibernate Tools 4.3.1.Final

import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;



/**
 * Home object for domain model class Productcoverage.
 * @see com.execmobile.data.Productcoverage
 * @author Hibernate Tools
 */
public class ProductcoverageHome {

	private static final Log log = LogFactory.getLog(ProductcoverageHome.class);

	private final SessionFactory sessionFactory;
	
	public ProductcoverageHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public void save(Productcoverage transientInstance) {
		log.debug("persisting Company instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			transientInstance.setProductCoverageId(UUID.randomUUID().toString());
			dbSession.save(transientInstance);
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			log.debug("save successful");
			
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
		
	}

	public void attachDirty(Productcoverage instance) {
		log.debug("attaching dirty Productcoverage instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Productcoverage instance) {
		log.debug("attaching clean Productcoverage instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Productcoverage persistentInstance) {
		log.debug("deleting Productcoverage instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			dbSession.delete(persistentInstance);
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public void deleteProductCoverage(String productID)
	{
		
	}

	public Productcoverage merge(Productcoverage detachedInstance) {
		log.debug("merging Productcoverage instance");
		try {
			Productcoverage result = (Productcoverage) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Productcoverage findById(java.lang.String id) {
		log.debug("getting Productcoverage instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Productcoverage instance = (Productcoverage) dbSession
					.get("com.execmobile.data.Productcoverage", id);
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
	public List<Productcoverage> getProductCoverage(String productID) {
		log.debug("finding Productpriceplan instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Productcoverage> productSupport = (List<Productcoverage>)dbSession.createCriteria("com.execmobile.data.Productcoverage")
			.add(Restrictions.eq("product.productId", productID)).list();
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			return productSupport;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
