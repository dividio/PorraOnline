package com.aap.rest.client;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Assertion {
	
	private String assertion;
	private String audience;
	
	public String getAssertion() {
		return assertion;
	}
	public void setAssertion(String assertion) {
		this.assertion = assertion;
	}
	public String getAudience() {
		return audience;
	}
	public void setAudience(String audience) {
		this.audience = audience;
	}
	
	
}