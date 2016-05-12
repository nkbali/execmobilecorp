package com.execmobile.data;

import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ZoneHome {

	private static final Log log = LogFactory.getLog(ZoneHome.class);

	private final SessionFactory sessionFactory;
	
	public ZoneHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Zone save(Zone transientInstance) {
		log.debug("persisting Company instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			transientInstance.setZoneid(UUID.randomUUID().toString());
			dbSession.save(transientInstance);
			dbTransaction.commit();
			log.debug("save successful");
			return transientInstance;
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
		
	}

	public void update(Zone instance) {
		log.debug("attaching dirty Product instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			dbSession.saveOrUpdate(instance);
			dbTransaction.commit();
			
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public Zone findById(java.lang.String id) {
		log.debug("getting Faq instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Zone instance = (Zone) dbSession.get("com.execmobile.data.Zone", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			dbTransaction.commit();
			
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Zone> getProductZone(String productID) {
		log.debug("finding Productpriceplan instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Zone> productZones = (List<Zone>)dbSession.createCriteria("com.execmobile.data.Zone")
			.add(Restrictions.eq("product.productId", productID))
			.addOrder(Order.asc("name")).list();
			dbTransaction.commit();
			return productZones;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	
	
}
