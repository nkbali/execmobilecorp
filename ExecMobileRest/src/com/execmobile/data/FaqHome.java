package com.execmobile.data;
// Generated Jan 31, 2016 10:08:30 PM by Hibernate Tools 4.3.1.Final

import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Home object for domain model class Faq.
 * @see com.execmobile.data.Faq
 * @author Hibernate Tools
 */
public class FaqHome {

	private static final Log log = LogFactory.getLog(FaqHome.class);

	private final SessionFactory sessionFactory;
	
	public FaqHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Faq save(Faq transientInstance) {
		log.debug("persisting Company instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			transientInstance.setFaqid(UUID.randomUUID().toString());
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

	public void update(Faq instance) {
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

	public void delete(Faq persistentInstance) {
		log.debug("deleting Faq instance");
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

	public Faq merge(Faq detachedInstance) {
		log.debug("merging Faq instance");
		try {
			Faq result = (Faq) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Faq findById(java.lang.String id) {
		log.debug("getting Faq instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Faq instance = (Faq) dbSession.get("com.execmobile.data.Faq", id);
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
	public List<Faq> getProductFaq(String productID) {
		log.debug("finding Productpriceplan instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Faq> productFaqs = (List<Faq>)dbSession.createCriteria("com.execmobile.data.Faq")
			.add(Restrictions.eq("product.productId", productID))
			.addOrder(Order.asc("priority")).list();
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			return productFaqs;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Faq> getProductFaqsBetweenPriority(String productID, int startPriority, int endPriority) {
		log.debug("finding Productpriceplan instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Faq> productFaqs = (List<Faq>)dbSession.createCriteria("com.execmobile.data.Faq")
			.add(Restrictions.eq("product.productId", productID))
			.add(Restrictions.between("priority", startPriority, endPriority))
			.addOrder(Order.asc("priority")).list();
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			return productFaqs;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Faq> getProductFaqsBelowPriority(String productID, int startPriority) {
		log.debug("finding Productpriceplan instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Faq> productFaqs = (List<Faq>)dbSession.createCriteria("com.execmobile.data.Faq")
			.add(Restrictions.eq("product.productId", productID))
			.add(Restrictions.ge("priority", startPriority))
			.addOrder(Order.asc("priority")).list();
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			return productFaqs;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
}
