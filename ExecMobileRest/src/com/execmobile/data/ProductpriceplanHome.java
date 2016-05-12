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
 * Home object for domain model class Productpriceplan.
 * @see com.execmobile.data.Productpriceplan
 * @author Hibernate Tools
 */
public class ProductpriceplanHome {

	private static final Log log = LogFactory.getLog(ProductpriceplanHome.class);

private final SessionFactory sessionFactory;
	
	public ProductpriceplanHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Productpriceplan save(Productpriceplan transientInstance) {
		log.debug("persisting Company instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			transientInstance.setProductPricePlanId(UUID.randomUUID().toString());
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

	public void update(Productpriceplan instance) {
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

	public void attachClean(Productpriceplan instance) {
		log.debug("attaching clean Productpriceplan instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Productpriceplan persistentInstance) {
		log.debug("deleting Productpriceplan instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Productpriceplan merge(Productpriceplan detachedInstance) {
		log.debug("merging Productpriceplan instance");
		try {
			Productpriceplan result = (Productpriceplan) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Productpriceplan findById(java.lang.String id) {
		log.debug("getting Productpriceplan instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Productpriceplan instance = (Productpriceplan) dbSession
					.get("com.execmobile.data.Productpriceplan", id);
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
	public Productpriceplan getProductPricePlan(String productID) {
		log.debug("finding Productpriceplan instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Productpriceplan> pricePlans = (List<Productpriceplan>)dbSession.createCriteria("com.execmobile.data.Productpriceplan")
			.add(Restrictions.eq("product.productId", productID)).list();
			if(pricePlans == null)
				return null;
			if(pricePlans.isEmpty())
				return null;
			Productpriceplan pricePlan = pricePlans.get(0);
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			return pricePlan;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
