package com.execmobile.data;
// Generated Jan 31, 2016 10:08:23 PM by Hibernate Tools 4.3.1.Final

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Faq generated by hbm2java
 */
public class Faq implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1279602831471044278L;
	private String faqid;
	private Product product;
	private String question;
	private String answer;
	private int priority;

	public Faq() {
	}

	public Faq(String faqid, Product product, String question, String answer, int priority) {
		this.faqid = faqid;
		this.product = product;
		this.question = question;
		this.answer = answer;
		this.priority = priority;
	}

	public String getFaqid() {
		return this.faqid;
	}

	public void setFaqid(String faqid) {
		this.faqid = faqid;
	}

	@JsonIgnore
	public Product getProduct() {
		return this.product;
	}

	@JsonProperty
	public void setProduct(Product product) {
		this.product = product;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
