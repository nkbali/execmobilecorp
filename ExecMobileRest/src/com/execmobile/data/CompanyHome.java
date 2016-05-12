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
import org.hibernate.criterion.Restrictions;

import com.execmobile.helpers.DBSessionManager;


/**
 * Home object for domain model class Company.
 * @see com.execmobile.data.Company
 * @author Hibernate Tools
 */
public class CompanyHome {

	private static final Log log = LogFactory.getLog(CompanyHome.class);

	private final SessionFactory sessionFactory;
	
	public CompanyHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
	public CompanyHome()
	{
		sessionFactory = new DBSessionManager().getSessionFactory();
	}
	
	public void closeSession()
	{
		this.sessionFactory.close();
	}

	public Company save(Company transientInstance) {
		log.debug("persisting Company instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			transientInstance.setCompanyId(UUID.randomUUID().toString());
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

	public void update(Company instance) {
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

	public void attachClean(Company instance) {
		log.debug("attaching clean Company instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Company persistentInstance) {
		log.debug("deleting Company instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Company merge(Company detachedInstance) {
		log.debug("merging Company instance");
		try {
			Company result = (Company) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Company findById(java.lang.String id) {
		log.debug("getting Company instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Company instance = (Company) dbSession.get("com.execmobile.data.Company", id);
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
	public List<Company> getCompanies() {
		log.debug("finding Company instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Company> results = (List<Company>) dbSession
					.createCriteria("com.execmobile.data.Company").addOrder(Order.asc("name")).list();
			log.debug("list companies successful, result size: " + results.size());
			dbTransaction.commit();
			if(dbSession.isConnected())
				dbSession.close();
			return results;
		} catch (RuntimeException re) {
			log.error("list companies failed", re);
			throw re;
		}
	}
	
	public Company findByUsername(String username){
		log.debug("finding Company instance by username");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			dbSession.beginTransaction();
			Company instance = (Company) dbSession
					.createCriteria("com.execmobile.data.Company")
					.add(Restrictions.eq("username", username)).uniqueResult();
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
	
	public Company findByToken(String accessToken){
		log.debug("finding Company instance by username");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			dbSession.beginTransaction();
			Company instance = (Company) dbSession
					.createCriteria("com.execmobile.data.Company")
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
