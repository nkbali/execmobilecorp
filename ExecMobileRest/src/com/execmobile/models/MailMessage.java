package com.execmobile.models;

import java.util.List;

public class MailMessage {
	
	private List<String> receivers;
	private String subject;
	private String body;
	/**
	 * @return the addresses
	 */
	public List<String> getReceivers() {
		return receivers;
	}
	/**
	 * @param addresses the addresses to set
	 */
	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
	

}
