package com.execmobile.data;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class Sessionlist implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6650258846838263071L;
	private String sessionId;
	private Company company;
	private String accessToken;
	@JsonIgnore
	private String lastAccessTime;
	
	public Sessionlist() {
	}

	public Sessionlist(String sessionId, Company company, String accessToken, String lastAccessTime) {
		this.sessionId = sessionId;
		this.company = company;
		this.accessToken = accessToken;
		this.lastAccessTime = lastAccessTime;
	}


	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	
	public Company getCompany() {
		return this.company;
	}


	public void setCompany(Company company) {
		this.company = company;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

}
