package com.execmobile.data;
// Generated Jan 31, 2016 10:08:30 PM by Hibernate Tools 4.3.1.Final

import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class Usage.
 * @see com.execmobile.data.Usage
 * @author Hibernate Tools
 */
public class UsageHome {

	private static final Log log = LogFactory.getLog(UsageHome.class);

	private final SessionFactory sessionFactory;
	
	public UsageHome(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Usage save(Usage transientInstance) {
		log.debug("persisting Usage instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			transientInstance.setUsageId(UUID.randomUUID().toString());
			dbSession.save(transientInstance);
			dbTransaction.commit();
			
			log.debug("persist successful");
			return transientInstance;
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	public void saveMonthlyUsage(List<Usage> usageRecords) {
		log.debug("persisting Usage instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			int i = 0;
			for (Usage usageRecord : usageRecords) {
				i++;
				usageRecord.setUsageId(UUID.randomUUID().toString());
				dbSession.save(usageRecord);
				if (i % 20 == 0) {
					dbSession.flush();
					dbSession.clear();
				}
			}
			dbTransaction.commit();
			
			log.debug("monthly usage save successful");

		} catch (RuntimeException re) {
			log.error("monthly usage save failed", re);
			throw re;
		}
	}

	public void update(Usage instance) {
		log.debug("attaching dirty Usage instance");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			dbSession.saveOrUpdate(instance);
			log.debug("attach successful");
			dbTransaction.commit();
			
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public Usage findById(java.lang.String id) {
		log.debug("getting Usage instance with id: " + id);
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			Usage instance = (Usage) dbSession.get("com.execmobile.data.Usage", id);
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
	public List<Usage> getUsageForMonth(Usage instance) {
		log.debug("finding Usage instance by example");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			List<Usage> results = (List<Usage>) dbSession
					.createCriteria("com.execmobile.data.Usage").add(create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			dbTransaction.commit();
			
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	@SuppressWarnings("rawtypes")
	public List getMonthlyUsage(int month, int year)
	{
		log.debug("getting monthlt usage");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			String sqlQuery = "SELECT usageRecord.UsageID, usageRecord.RecordDate, usageRecord.Bundles, usageRecord.Zone, usageRecord.Base, usageRecord.Ext, UsageRecord.Total, usageRecord.DeviceID, company.Name, country.CountryID, country.CountryName FROM execmobile_usage.`usage` AS usageRecord INNER JOIN execmobile_usage.device As device ON device.DeviceID = usageRecord.DeviceID INNER JOIN execmobile_usage.country As country ON country.CountryID = usageRecord.CountryId INNER JOIN execmobile_usage.company As company ON company.CompanyID = device.CompanyID WHERE MONTH(STR_TO_DATE(RecordDate, '%e/%c/%Y'))=" + month + " AND YEAR(STR_TO_DATE(RecordDate, '%e/%c/%Y'))=" + year;
			SQLQuery usageQuery = dbSession.createSQLQuery(sqlQuery);
			usageQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List usageRecords = usageQuery.list();
			dbTransaction.commit();
			log.debug("monthly usage fetched successfuly");
			return usageRecords;
			
		} catch (RuntimeException re) {
			log.error("fetch failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List getYearlyUsageReport(int year)
	{
		log.debug("getting monthlt usage");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			String sqlQuery = "SELECT device.DeviceID, product.Name, company.Name AS Company, device.IsActive, SUM(usageRecord.Total) AS MonthlyTotal, CONCAT_WS('',YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')),LPAD(MONTH(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')),2,0)) AS UsageMonth \r\n" + 
					"					FROM execmobile_usage.`usage` AS usageRecord  \r\n" + 
					"					INNER JOIN execmobile_usage.device As device ON device.DeviceID = usageRecord.DeviceID \r\n" + 
					"					INNER JOIN execmobile_usage.product As product ON device.ProductID = product.ProductID \r\n" + 
					"					INNER JOIN execmobile_usage.country As country ON country.CountryID = usageRecord.CountryID\r\n" + 
					"					INNER JOIN execmobile_usage.company As company ON device.CompanyID = company.CompanyID \r\n" + 
					"					WHERE YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y'))=" + year + "\r\n" + 
					"					GROUP BY YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')), MONTH(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')), device.DeviceID ORDER BY MONTH(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y'))";
			SQLQuery usageQuery = dbSession.createSQLQuery(sqlQuery);
			usageQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List yearlyUsageRecordsForCompany = usageQuery.list();
			dbTransaction.commit();
			log.debug("monthly usage fetched successfuly");
			return yearlyUsageRecordsForCompany;
			
		} catch (RuntimeException re) {
			log.error("fetch failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List getYearlyUsageReportForCompany(String companyID, int year)
	{
		log.debug("getting monthlt usage");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			String sqlQuery = "SELECT device.DeviceID, product.Name, company.Name AS Company, device.IsActive, SUM(usageRecord.Total) AS MonthlyTotal, CONCAT_WS('',YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')),LPAD(MONTH(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')),2,0)) AS UsageMonth\r\n" + 
					"FROM execmobile_usage.`usage` AS usageRecord \r\n" + 
					"INNER JOIN execmobile_usage.device As device ON device.DeviceID = usageRecord.DeviceID\r\n" + 
					"INNER JOIN execmobile_usage.product As product ON device.ProductID = product.ProductID\r\n" + 
					"INNER JOIN execmobile_usage.country As country ON country.CountryID = usageRecord.CountryID\r\n"+
					"INNER JOIN execmobile_usage.company As company ON device.CompanyID = company.CompanyID\r\n" + 
					"WHERE YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y'))=" + year +" AND company.CompanyID='" + companyID +"'\r\n" + 
					"GROUP BY YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')), MONTH(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')), device.DeviceID ORDER BY MONTH(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y'))";
			SQLQuery usageQuery = dbSession.createSQLQuery(sqlQuery);
			usageQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List yearlyUsageRecordsForCompany = usageQuery.list();
			dbTransaction.commit();
			log.debug("monthly usage fetched successfuly");
			return yearlyUsageRecordsForCompany;
			
		} catch (RuntimeException re) {
			log.error("fetch failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List getYearlyUsageReportForDevice(String deviceID, int year)
	{
		log.debug("getting monthlt usage");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			String sqlQuery = "SELECT device.DeviceID, product.Name, company.Name AS Company, device.IsActive, SUM(usageRecord.Total) AS MonthlyTotal, CONCAT_WS('',YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')),LPAD(MONTH(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')),2,0)) AS UsageMonth\r\n" + 
					"FROM execmobile_usage.`usage` AS usageRecord \r\n" + 
					"INNER JOIN execmobile_usage.device As device ON device.DeviceID = usageRecord.DeviceID\r\n" + 
					"INNER JOIN execmobile_usage.product As product ON device.ProductID = product.ProductID\r\n" + 
					"INNER JOIN execmobile_usage.country As country ON country.CountryID = usageRecord.CountryID\r\n"+
					"INNER JOIN execmobile_usage.company As company ON device.CompanyID = company.CompanyID\r\n" + 
					"WHERE YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y'))=" + year +" AND device.DeviceID='" + deviceID +"'\r\n" + 
					"GROUP BY YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')), MONTH(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y')), device.DeviceID ORDER BY MONTH(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y'))";
			SQLQuery usageQuery = dbSession.createSQLQuery(sqlQuery);
			usageQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List yearlyUsageRecordsForCompany = usageQuery.list();
			dbTransaction.commit();
			log.debug("monthly usage fetched successfuly");
			return yearlyUsageRecordsForCompany;
			
		} catch (RuntimeException re) {
			log.error("fetch failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List getYearlyUsageReportByLocation(int year)
	{
		log.debug("getting monthlt usage");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			String sqlQuery = "SELECT device.DeviceID, product.Name, company.Name AS Company, device.IsActive, COUNT(country.CountryName) AS Total, country.CountryName AS Location\r\n" + 
					"FROM execmobile_usage.`usage` AS usageRecord \r\n" + 
					"INNER JOIN execmobile_usage.device As device ON device.DeviceID = usageRecord.DeviceID\r\n" + 
					"INNER JOIN execmobile_usage.product As product ON device.ProductID = product.ProductID\r\n" + 
					"INNER JOIN execmobile_usage.company As company ON device.CompanyID = company.CompanyID\r\n" + 
					"INNER JOIN execmobile_usage.country As country ON country.CountryID = usageRecord.CountryId\r\n" + 
					" WHERE YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y'))=" + year + "\r\n" + 
					"GROUP BY usageRecord.CountryId, device.DeviceID\r\n" + 
					"ORDER BY country.CountryName";
			SQLQuery usageQuery = dbSession.createSQLQuery(sqlQuery);
			usageQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List yearlyUsageRecordsForCompany = usageQuery.list();
			dbTransaction.commit();
			log.debug("monthly usage fetched successfuly");
			return yearlyUsageRecordsForCompany;
			
		} catch (RuntimeException re) {
			log.error("fetch failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List getYearlyUsageReportForCompanyByLocation(String companyID, int year)
	{
		log.debug("getting monthlt usage");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			String sqlQuery = "SELECT device.DeviceID, product.Name, company.Name AS Company, device.IsActive, COUNT(country.CountryName) AS Total, country.CountryName AS Location\r\n" + 
					"FROM execmobile_usage.`usage` AS usageRecord \r\n" + 
					"INNER JOIN execmobile_usage.device As device ON device.DeviceID = usageRecord.DeviceID\r\n" + 
					"INNER JOIN execmobile_usage.product As product ON device.ProductID = product.ProductID\r\n" + 
					"INNER JOIN execmobile_usage.company As company ON device.CompanyID = company.CompanyID\r\n" + 
					"INNER JOIN execmobile_usage.country As country ON country.CountryID = usageRecord.CountryId\r\n" + 
					" WHERE YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y'))=" + year + " AND company.CompanyID='" + companyID + "'\r\n" + 
					"GROUP BY usageRecord.CountryId, device.DeviceID\r\n" + 
					"ORDER BY country.CountryName";
			SQLQuery usageQuery = dbSession.createSQLQuery(sqlQuery);
			usageQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List yearlyUsageRecordsForCompany = usageQuery.list();
			dbTransaction.commit();
			log.debug("monthly usage fetched successfuly");
			return yearlyUsageRecordsForCompany;
			
		} catch (RuntimeException re) {
			log.error("fetch failed", re);
			throw re;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List getYearlyUsageReportForDeviceByLocation(String deviceID, int year)
	{
		log.debug("getting monthlt usage");
		try {
			Session dbSession = sessionFactory.getCurrentSession();
			Transaction dbTransaction = dbSession.beginTransaction();
			String sqlQuery = "SELECT device.DeviceID, product.Name, company.Name AS Company, device.IsActive, COUNT(country.CountryName) AS Total, country.CountryName AS Location\r\n" + 
					"FROM execmobile_usage.`usage` AS usageRecord \r\n" + 
					"INNER JOIN execmobile_usage.device As device ON device.DeviceID = usageRecord.DeviceID\r\n" + 
					"INNER JOIN execmobile_usage.product As product ON device.ProductID = product.ProductID\r\n" + 
					"INNER JOIN execmobile_usage.company As company ON device.CompanyID = company.CompanyID\r\n" + 
					"INNER JOIN execmobile_usage.country As country ON country.CountryID = usageRecord.CountryId\r\n" + 
					" WHERE YEAR(STR_TO_DATE(usageRecord.RecordDate, '%e/%c/%Y'))=" + year + " AND device.DeviceID='" + deviceID + "'\r\n" + 
					"GROUP BY usageRecord.CountryId, device.DeviceID\r\n" + 
					"ORDER BY country.CountryName";
			SQLQuery usageQuery = dbSession.createSQLQuery(sqlQuery);
			usageQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List yearlyUsageRecordsForCompany = usageQuery.list();
			dbTransaction.commit();
			log.debug("monthly usage fetched successfuly");
			return yearlyUsageRecordsForCompany;
			
		} catch (RuntimeException re) {
			log.error("fetch failed", re);
			throw re;
		}
	}

}
