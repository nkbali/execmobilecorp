package com.execmobile.data;
// Generated Jan 31, 2016 10:08:30 PM by Hibernate Tools 4.3.1.Final

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.execmobile.helpers.DBSessionManager;


/**
 * Home object for domain model class Device.
 * @see com.execmobile.data.Device
 * @author Hibernate Tools
 */
public class DeviceHome {

	private static final Log log = LogFactory.getLog(DeviceHome.class);

	private final SessionFactory sessionFactory;
	
	public DeviceHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public DeviceHome()
	{
		sessionFactory = new DBSessionManager().getSessionFactory();
	}
	
	public void closeSession()
	{
		this.sessionFactory.close();
	}
	
	public Device save(Device transientInstance) {
		log.debug("persisting Device instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
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
	
	public void saveDevices(List<Device> devices) {
		log.debug("persisting Usage instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			int i = 0;
			for (Device device : devices) {
				i++;
				
				dbSession.save(device);
				if (i % 20 == 0) {
					dbSession.flush();
					dbSession.clear();
				}
			}
			dbTransaction.commit();
			if (dbSession.isConnected())
				dbSession.close();
			log.debug("monthly usage save successful");

		} catch (RuntimeException re) {
			log.error("monthly usage save failed", re);
			throw re;
		}
	}

	public void update(Device instance) {
		log.debug("attaching dirty Device instance");
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
	
	public void updateDevices(List<Device> devices) {
		log.debug("attaching dirty Device instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			int i = 0;
			for (Device device : devices) {
				i++;
				device.setIsActive(true);
				dbSession.saveOrUpdate(device);
				if (i % 20 == 0) {
					dbSession.flush();
					dbSession.clear();
				}
			}
			dbTransaction.commit();
			if (dbSession.isConnected())
				dbSession.close();
			log.debug("monthly usage save successful");

		} catch (RuntimeException re) {
			log.error("monthly usage save failed", re);
			throw re;
		}
	}

	public void attachClean(Device instance) {
		log.debug("attaching clean Device instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Device persistentInstance) {
		log.debug("deleting Device instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Device merge(Device detachedInstance) {
		log.debug("merging Device instance");
		try {
			Device result = (Device) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Device findById(java.lang.String id) {
		log.debug("getting Device instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Device instance = (Device) dbSession.get("com.execmobile.data.Device", id);
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
	public List<Device> getCompanyDevices(String companyID) {
		log.debug("finding Device instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Device> results = (List<Device>) dbSession
					.createCriteria("com.execmobile.data.Device").
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
	
	@SuppressWarnings("unchecked")
	public List<Device> getAvailableDevices() {
		log.debug("finding Device instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Device> results = (List<Device>) dbSession
					.createCriteria("com.execmobile.data.Device").
					add(Restrictions.isNull("company.companyId")).list();
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
	public List<Device> getAllocatedDevices() {
		log.debug("finding Device instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Device> results = (List<Device>) dbSession
					.createCriteria("com.execmobile.data.Device").
					add(Restrictions.isNotNull("company.companyId")).list();
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
	public List<Device> searchDevices(String searchText) {
		log.debug("finding Device instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Criterion deviceRestriction= Restrictions.or(Restrictions.ilike("deviceId", searchText, MatchMode.ANYWHERE), 
					Restrictions.ilike("imei", searchText, MatchMode.ANYWHERE));
			List<Device> results = (List<Device>) dbSession
					.createCriteria("com.execmobile.data.Device").
					add(Restrictions.and(Restrictions.isNotNull("company.companyId"), deviceRestriction)).list();
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
	
	public Device findByImei(java.lang.String imei) {
		log.debug("getting Device instance with imei: " + imei);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			dbSession.beginTransaction();
			Device instance = (Device) dbSession
					.createCriteria("com.execmobile.data.Device")
					.add(Restrictions.eq("imei", imei)).uniqueResult();
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
