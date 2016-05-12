package com.execmobile.data;
// Generated Jan 31, 2016 10:08:30 PM by Hibernate Tools 4.3.1.Final

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.execmobile.helpers.DBSessionManager;
import com.execmobile.helpers.HistorySorter;

import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class Apploginhistory.
 * @see com.execmobile.data.Apploginhistory
 * @author Hibernate Tools
 */
public class ApploginhistoryHome {

	private static final Log log = LogFactory.getLog(ApploginhistoryHome.class);

private final SessionFactory sessionFactory;
	
	public ApploginhistoryHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	public ApploginhistoryHome()
	{
		sessionFactory = new DBSessionManager().getSessionFactory();
	}
	
	public void closeSession()
	{
		this.sessionFactory.close();
	}

	public Apploginhistory save(Apploginhistory transientInstance) {
		log.debug("persisting Apploginhistory instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			transientInstance.setAppLoginHistoryId(UUID.randomUUID().toString());
			dbSession.save(transientInstance);
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			log.debug("persist successful");
			return transientInstance;
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void update(Apploginhistory instance) {
		log.debug("attaching dirty Apploginhistory instance");
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
	
	public void delete(Apploginhistory instance) {
		log.debug("attaching dirty Apploginhistory instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			dbSession.delete(instance);
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public Apploginhistory findById(java.lang.String id) {
		log.debug("getting Apploginhistory instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			dbSession.beginTransaction();
			Apploginhistory instance = (Apploginhistory) dbSession
					.get("com.execmobile.data.Apploginhistory", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			dbSession.close();
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public Apploginhistory findByToken(java.lang.String token) {
		log.debug("getting Apploginhistory instance with token: " + token);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			dbSession.beginTransaction();
			Apploginhistory instance = (Apploginhistory) dbSession
					.createCriteria("com.execmobile.data.Apploginhistory")
					.add(Restrictions.eq("token", token)).uniqueResult();
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			dbSession.close();
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Apploginhistory> findByEmail(Apploginhistory instance) {
		log.debug("finding Apploginhistory instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Apploginhistory> results = (List<Apploginhistory>) dbSession
					.createCriteria("com.execmobile.data.Apploginhistory").add(create(instance))
					.add(Restrictions.eq("email", instance.getEmail())).list();
			log.debug("find by example successful, result size: " + results.size());
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Apploginhistory> findAppUsers() {
		log.debug("finding Apploginhistory instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Criteria criteria = dbSession.createCriteria(Apploginhistory.class);
			List<Apploginhistory> results = criteria.list();
			log.debug("find by example successful, result size: " + results.size());
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			Collections.sort(results, new HistorySorter());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
