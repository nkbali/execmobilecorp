package com.execmobile.data;
// Generated Jan 31, 2016 10:08:23 PM by Hibernate Tools 4.3.1.Final

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Apploginhistory generated by hbm2java
 */
public class Apploginhistory implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3407402636222375370L;
	private String appLoginHistoryId;
	@JsonIgnore
	private Device device;
	private String email;
	@JsonIgnore
	private String token;

	private String lastAccessTime;

	public Apploginhistory() {
	}

	public Apploginhistory(String appLoginHistoryId, Device device, String email, String token, String lastAccessTime) {
		this.appLoginHistoryId = appLoginHistoryId;
		this.device = device;
		this.email = email;
		this.token = token;
		this.lastAccessTime = lastAccessTime;
	}

	public String getAppLoginHistoryId() {
		return this.appLoginHistoryId;
	}

	public void setAppLoginHistoryId(String appLoginHistoryId) {
		this.appLoginHistoryId = appLoginHistoryId;
	}

	public Device getDevice() {
		return this.device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getLastAccessTime() {
		return this.lastAccessTime;
	}

	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

}
