package com.execmobile.data;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.execmobile.helpers.DBSessionManager;

public class SessionlistHome {
	
	private static final Log log = LogFactory.getLog(SessionlistHome.class);

	private final SessionFactory sessionFactory;
	
	public SessionlistHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	public SessionlistHome()
	{
		sessionFactory = new DBSessionManager().getSessionFactory();
	}
	
	public void closeSession()
	{
		this.sessionFactory.close();
	}
	
	public Sessionlist save(Sessionlist transientInstance) {
		log.debug("persisting Company instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
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

	public void update(Sessionlist instance) {
		log.debug("attaching dirty Company instance");
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
	
	public void removeSession(Sessionlist instance) {
		log.debug("attaching dirty Company instance");
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
	
	public Sessionlist findById(java.lang.String id) {
		log.debug("getting Company instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Sessionlist instance = (Sessionlist) dbSession.get("com.execmobile.data.Sessionlist", id);
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
	public List<Sessionlist> getCompanySessions(String companyID) {
		log.debug("finding Device instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Sessionlist> results = (List<Sessionlist>) dbSession
					.createCriteria("com.execmobile.data.Sessionlist").
					add(Restrictions.eq("company.companyId", companyID)).list();
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
	
	public Sessionlist findByToken(String accessToken){
		log.debug("finding Company instance by username");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			dbSession.beginTransaction();
			Sessionlist instance = (Sessionlist) dbSession
					.createCriteria("com.execmobile.data.Sessionlist")
					.add(Restrictions.eq("accessToken", accessToken)).uniqueResult();
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

}
