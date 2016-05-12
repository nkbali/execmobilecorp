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
 * Home object for domain model class Subscriber.
 * @see com.execmobile.data.Subscriber
 * @author Hibernate Tools
 */
public class SubscriberHome {

	private static final Log log = LogFactory.getLog(SubscriberHome.class);

	private final SessionFactory sessionFactory;
	
	public SubscriberHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public void persist(Subscriber transientInstance) {
		log.debug("persisting Subscriber instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Subscriber instance) {
		log.debug("attaching dirty Subscriber instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Subscriber instance) {
		log.debug("attaching clean Subscriber instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Subscriber persistentInstance) {
		log.debug("deleting Subscriber instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Subscriber merge(Subscriber detachedInstance) {
		log.debug("merging Subscriber instance");
		try {
			Subscriber result = (Subscriber) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Subscriber findById(java.lang.String id) {
		log.debug("getting Subscriber instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Subscriber instance = (Subscriber) dbSession
					.get("com.execmobile.data.Subscriber", id);
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
	public List<Subscriber> findByExample(Subscriber instance) {
		log.debug("finding Subscriber instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Subscriber> results = (List<Subscriber>) dbSession
					.createCriteria("com.execmobile.data.Subscriber").add(create(instance)).list();
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
