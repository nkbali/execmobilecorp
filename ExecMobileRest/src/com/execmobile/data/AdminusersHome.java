package com.execmobile.data;
// Generated Jan 31, 2016 10:08:30 PM by Hibernate Tools 4.3.1.Final

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class Adminusers.
 * @see com.execmobile.data.Adminusers
 * @author Hibernate Tools
 */
public class AdminusersHome {

	private static final Log log = LogFactory.getLog(AdminusersHome.class);

	private final SessionFactory sessionFactory;
	
	public AdminusersHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public void persist(Adminusers transientInstance) {
		log.debug("persisting Adminusers instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Adminusers instance) {
		log.debug("attaching dirty Adminusers instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Adminusers instance) {
		log.debug("attaching clean Adminusers instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Adminusers persistentInstance) {
		log.debug("deleting Adminusers instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Adminusers merge(Adminusers detachedInstance) {
		log.debug("merging Adminusers instance");
		try {
			Adminusers result = (Adminusers) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Adminusers findById(java.lang.String id) {
		log.debug("getting Adminusers instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			dbSession.beginTransaction();
			Adminusers instance = (Adminusers) dbSession
					.get("com.execmobile.data.Adminusers", id);
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
	public List<Adminusers> findByExample(Adminusers instance) {
		log.debug("finding Adminusers instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Adminusers> results = (List<Adminusers>) dbSession
					.createCriteria("com.execmobile.data.Adminusers").add(create(instance)).list();
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
}
