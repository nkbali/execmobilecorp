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
import org.hibernate.criterion.Order;


/**
 * Home object for domain model class Country.
 * @see com.execmobile.data.Country
 * @author Hibernate Tools
 */
public class CountryHome {

	private static final Log log = LogFactory.getLog(CountryHome.class);

	private final SessionFactory sessionFactory;
	
	public CountryHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Country save(Country transientInstance) {
		log.debug("persisting Country instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			transientInstance.setCountryId(UUID.randomUUID().toString());
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

	public void update(Country instance) {
		log.debug("attaching dirty Country instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Country instance) {
		log.debug("attaching clean Country instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Country persistentInstance) {
		log.debug("deleting Country instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Country merge(Country detachedInstance) {
		log.debug("merging Country instance");
		try {
			Country result = (Country) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Country findById(java.lang.String id) {
		log.debug("getting Country instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Country instance = (Country) dbSession.get("com.execmobile.data.Country", id);
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
	public List<Country> getCountries() {
		log.debug("finding all countries");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Country> results = (List<Country>) dbSession
					.createCriteria("com.execmobile.data.Country").addOrder(Order.asc("countryName")).list();
			log.debug("find by example successful, result size: " + results.size());
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			return results;
		} catch (RuntimeException re) {
			log.error("get countries list failed", re);
			throw re;
		}
	}

	
}
