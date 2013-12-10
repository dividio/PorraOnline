package com.aap.rest.client;

import java.util.Date;

import com.aap.dto.Usuarios;

public class VerificationResult {

	/**
	 * May be either "okay" or "failure".
	 */
	private String status;
	
	/**
	 * A string explaining why verification failed.
	 */
	private String reason;
	
	/**
	 * The address contained in the assertion, for the intended person being logged in.
	 */
	private String email;
	
	/**
	 * The audience value contained in the assertion. Expected to be your own website URL.
	 */
	private String audience;
	
	/**
	 * The date the assertion expires, expressed as the primitive value of a Date object: that is, the number of milliseconds since midnight 01 January, 1970 UTC.
	 */
	private Date expires;
	
	/**
	 * The hostname of the identity provider that issued the assertion.
	 */
	private String issuer;
	
	private Usuarios usuario;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}
	
}
